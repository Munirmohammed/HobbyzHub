package com.hobbyzhub.javabackend.categoriesmodule;

import com.hobbyzhub.javabackend.categoriesmodule.abstractions.AbstractCategoryControllerDef;
import com.hobbyzhub.javabackend.categoriesmodule.entity.HobbyCategory;
import com.hobbyzhub.javabackend.categoriesmodule.payload.request.GetDeleteCategoryRequest;
import com.hobbyzhub.javabackend.categoriesmodule.payload.request.SearchCategoryRequest;
import com.hobbyzhub.javabackend.categoriesmodule.payload.response.CRUHobbyCategoryResponse;
import com.hobbyzhub.javabackend.categoriesmodule.service.HobbyCategoryService;
import com.hobbyzhub.javabackend.sharedexceptions.ServerErrorException;
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

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/categories/hobby")
@Slf4j
public class HobbyController extends AbstractCategoryControllerDef<HobbyCategory> {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HobbyCategoryService hobbyCategoryService;

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @Override
    @PostMapping(value = "/create-hobby", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCategory(
        @RequestParam(required = false, name = "iconImage") MultipartFile iconImage,
        @RequestParam(name = "categoryName") String categoryName,
        @RequestParam(required = false, name = "hobbyCategoryId") String hobbyCategoryId
    ) {
        try {
            HobbyCategory newCategory = hobbyCategoryService.createCategory(categoryName, iconImage, null);
            log.info("Successfully created new hobby category");

            CRUHobbyCategoryResponse response = this.mapEntityToPayload(newCategory);
            return ResponseEntity.ok(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully created new category",
                true,
                HttpStatus.OK.value(),
                response
            ));
        } catch(Exception ex) {
            log.error("Exception when creating new hobby category: " + ex.getMessage());
            throw new ServerErrorException(ex.getMessage());
        }
    }

    @Override
    @PutMapping(value = "/update-hobby", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCategory(
        @RequestParam(required = false, name = "iconImage") MultipartFile iconImage,
        @RequestParam(name = "categoryId") String categoryId,
        @RequestParam(name = "categoryName") String categoryName) {
    	try {
            HobbyCategory updatedCategory = hobbyCategoryService.updateCategory(categoryName, categoryId, iconImage);
            log.info("Successfully updated hobby category with id " + categoryId);

            CRUHobbyCategoryResponse response = this.mapEntityToPayload(updatedCategory);
            return ResponseEntity.ok(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully created new category",
                true,
                HttpStatus.OK.value(),
                response
            ));
        } catch(Exception ex) {
            log.error("Exception when creating new hobby category: " + ex.getMessage());
            throw new ServerErrorException(ex.getMessage());
        }
    }
    
    @Override
    @PostMapping(value = "/get-hobby", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCategory(@RequestBody GetDeleteCategoryRequest request) {
        try {
            HobbyCategory existingCategory = hobbyCategoryService.findCategoryById(request.getCategoryId());
            log.info("Successfully retrieved hobby category with id " + request.getCategoryId());

            CRUHobbyCategoryResponse response = this.mapEntityToPayload(existingCategory);
            return ResponseEntity.ok(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully created new category",
                true,
                HttpStatus.OK.value(),
                response
            ));
        } catch(EntityNotFoundException ex) {
            log.error("Exception when creating new hobby category: " + ex.getMessage());
            throw new ServerErrorException(ex.getMessage());
        }
    }
    
    @PostMapping(value = "/get-list")
    public ResponseEntity<?> getHobbyCategoryList(@RequestBody GetDeleteCategoryRequest request) {
        List<HobbyCategory> hobbysList = hobbyCategoryService.getPagedCategoryList(request.getPage(), request.getSize());
        List<CRUHobbyCategoryResponse> responseList = hobbysList.stream().map(this::mapEntityToPayload).toList();
        log.info("Successfully retrieved hobby category list");

        return ResponseEntity.ok(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully created new category",
            true,
            HttpStatus.OK.value(),
            responseList
        ));
    }

    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchBySlug(@RequestBody SearchCategoryRequest request) {
        List<HobbyCategory> matchingResults = hobbyCategoryService.searchCategoryByName(request.getSearchSlug(), request.getPage(), request.getSize());
        log.info("Matching results length: {}", matchingResults.size());
        List<CRUHobbyCategoryResponse> responseList = matchingResults.stream().map(this::mapEntityToPayload).toList();
        log.info("Successfully retrieved hobby category list");

        return ResponseEntity.ok(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully retrieved hobby category list",
            true,
            HttpStatus.OK.value(),
            responseList
        ));
    }
    
    @Override
    @DeleteMapping(value = "/delete-hobby", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteHobbyCategory(@RequestBody GetDeleteCategoryRequest request) {
        try {
            hobbyCategoryService.deleteCategory(request.getCategoryId());
            log.info("Category with id: " + request.getCategoryId() + " deleted successfully");
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

    private CRUHobbyCategoryResponse mapEntityToPayload(HobbyCategory entity) {
        CRUHobbyCategoryResponse responsePayload = modelMapper.map(entity, CRUHobbyCategoryResponse.class);
        responsePayload.setLastEdited(entity.getLastEdited().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        return responsePayload;
    }
}

        