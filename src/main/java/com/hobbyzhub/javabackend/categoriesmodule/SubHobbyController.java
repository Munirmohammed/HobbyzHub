package com.hobbyzhub.javabackend.categoriesmodule;

import com.hobbyzhub.javabackend.categoriesmodule.abstractions.AbstractCategoryControllerDef;
import com.hobbyzhub.javabackend.categoriesmodule.entity.HobbySubCategory;
import com.hobbyzhub.javabackend.categoriesmodule.payload.request.GetDeleteCategoryRequest;
import com.hobbyzhub.javabackend.categoriesmodule.payload.request.SearchCategoryRequest;
import com.hobbyzhub.javabackend.categoriesmodule.payload.response.CRUHobbyCategoryResponse;
import com.hobbyzhub.javabackend.categoriesmodule.service.HobbySubCategoryService;
import com.hobbyzhub.javabackend.categoriesmodule.service.SubHobbySubscriberService;
import com.hobbyzhub.javabackend.sharedpayload.GenericResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.hobbyzhub.javabackend.sharedexceptions.ServerErrorException;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/categories/sub-hobby")
@Slf4j
public class SubHobbyController extends AbstractCategoryControllerDef<HobbySubCategory> {
	@Autowired
	private ModelMapper modelMapper;

    @Autowired
    private HobbySubCategoryService hobbySubCategoryService;

	@Autowired
    private SubHobbySubscriberService subHobbySubscriberService;

	@Value("${application.api.version}")
	private String apiVersion;

	@Value("${application.organization.name}")
	private String organizationName;

	@Override
	public ResponseEntity<?> createCategory(MultipartFile iconImage, String categoryName, String parentCategoryId) {
		return null;
	}

	@Override
	public ResponseEntity<?> updateCategory(MultipartFile iconImage, String categoryId, String categoryName) {
		return null;
	}

	@PostMapping(value ="/create-subhobby")
    public ResponseEntity<?> createCategory(
		@RequestParam(name = "categoryName") String categoryName,
		@RequestParam(name = "parentCategoryId") String parentCategoryId) {
		try {
			HobbySubCategory newSubCategory = hobbySubCategoryService.createCategory(categoryName, parentCategoryId);
			log.info("Successfully created new sub hobby");

			CRUHobbyCategoryResponse response = this.mapEntityToPayload(newSubCategory);
			return ResponseEntity.ok(new GenericResponse<>(
				apiVersion,
				organizationName,
				"Successfully created new sub hobby",
				true,
				HttpStatus.OK.value(),
				response
			));
		} catch(EntityNotFoundException ex) {
			log.error("Not found parent category with id " + parentCategoryId);
			throw new ServerErrorException(ex.getMessage());
		}
	}
    
    @PutMapping(value = "/update-subhobby")
    public ResponseEntity<?> updateCategory(
		@RequestParam(name = "categoryId") String categoryId,
		@RequestParam(name = "categoryName") String categoryName) {
		try {
			HobbySubCategory updatedSubCategory = hobbySubCategoryService.updateCategory(categoryName, categoryId);
			log.info("Successfully updated sub category details");
			CRUHobbyCategoryResponse response = this.mapEntityToPayload(updatedSubCategory);
			return ResponseEntity.ok(new GenericResponse<>(
				apiVersion,
				organizationName,
				"Successfully updated sub hobby details",
				true,
				HttpStatus.OK.value(),
				response
			));
		} catch(EntityNotFoundException ex) {
			log.error("Not found sub category with id " + categoryId);
			throw new ServerErrorException(ex.getMessage());
		}
    }

	@Override
	@PostMapping(value = "/get-by-id")
	public ResponseEntity<?> getCategory(@RequestBody GetDeleteCategoryRequest request) {
		try {
			HobbySubCategory updatedSubCategory = hobbySubCategoryService.findCategoryById(request.getCategoryId());
			log.info("Successfully gotten sub category");
			CRUHobbyCategoryResponse response = this.mapEntityToPayload(updatedSubCategory);
			return ResponseEntity.ok(new GenericResponse<>(
					apiVersion,
					organizationName,
					"Successfully gotten sub category",
					true,
					HttpStatus.OK.value(),
					response
			));
		} catch(EntityNotFoundException ex) {
			log.error("Not found sub category with id " + request.getCategoryId());
			throw new ServerErrorException(ex.getMessage());
		}
	}

	/**
	 *
	 * @param request is carrying the id of the hobby that we are retrieving the list of sub hobbies for
	 * @return a ResponseEntity with the request body having the full list of sub-hobbies
	 */
	@PostMapping(value = "/get-list")
    public ResponseEntity<?> getSubHobbyList(@RequestBody GetDeleteCategoryRequest request) {
		try {
			List<HobbySubCategory> hobbySubCategories = hobbySubCategoryService.getPagedCategory(request.getCategoryId());
			log.info("Successfully gotten sub category list");
			List<CRUHobbyCategoryResponse> responseList = hobbySubCategories.parallelStream().map(this::mapEntityToPayload).toList();
			return ResponseEntity.ok(new GenericResponse<>(
				apiVersion,
				organizationName,
				"Successfully gotten sub category list",
				true,
				HttpStatus.OK.value(),
				responseList
			));
		} catch(EntityNotFoundException ex) {
			log.error("Not found parent category with id " + request.getCategoryId());
			throw new ServerErrorException(ex.getMessage());
		}
    }

	@PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> searchByCategoryName(@RequestBody SearchCategoryRequest request) {
		List<HobbySubCategory> matchingResults = hobbySubCategoryService.searchCategoryByName(request.getSearchSlug(), request.getPage(), request.getSize());
		List<CRUHobbyCategoryResponse> responseList = matchingResults.parallelStream().map(this::mapEntityToPayload).toList();
		return ResponseEntity.ok(new GenericResponse<>(
			apiVersion,
			organizationName,
			"Successfully gotten sub category list",
			true,
			HttpStatus.OK.value(),
			responseList
		));
	}
    
    @Override
	@DeleteMapping(value = "/delete-subhobby", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteHobbyCategory(@RequestBody GetDeleteCategoryRequest request) {
		try {
			hobbySubCategoryService.deleteCategory(request.getCategoryId());
			log.info("Successfully deleted sub hobby with id: " + request.getCategoryId());
			return ResponseEntity.ok(new GenericResponse<>(
					apiVersion,
					organizationName,
					"Successfully deleted category",
					true,
					HttpStatus.OK.value(),
					null
			));
		} catch(EntityNotFoundException ex) {
			log.error("Category with id: " + request.getCategoryId() + " not found for deletion");
			throw new ServerErrorException(ex.getMessage());
		}
    }

	private CRUHobbyCategoryResponse mapEntityToPayload(HobbySubCategory entity) {
		CRUHobbyCategoryResponse responsePayload = modelMapper.map(entity, CRUHobbyCategoryResponse.class);
		responsePayload.setLastEdited(entity.getLastEdited().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

		return responsePayload;
	}
}
