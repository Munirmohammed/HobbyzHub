package com.hobbyzhub.javabackend.categoriesmodule.service;

import java.util.List;

import com.hobbyzhub.javabackend.categoriesmodule.abstractions.AbstractEntityHelperDef;
import com.hobbyzhub.javabackend.categoriesmodule.abstractions.CategoryServiceDef;
import com.hobbyzhub.javabackend.categoriesmodule.entity.HobbyCategory;
import com.hobbyzhub.javabackend.categoriesmodule.entity.HobbySubCategory;
import com.hobbyzhub.javabackend.categoriesmodule.repository.HobbyCategoryRepository;
import com.hobbyzhub.javabackend.categoriesmodule.repository.HobbySubCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class HobbySubCategoryService extends AbstractEntityHelperDef<HobbySubCategory> implements CategoryServiceDef<HobbySubCategory> {
    @Autowired
    private HobbySubCategoryRepository hobbySubCategoryRepository;

    @Autowired
    private HobbyCategoryRepository hobbyCategoryRepository;

    @Override
    public HobbySubCategory createCategory(String categoryName, MultipartFile iconImage, String categoryId) {
        return null;
    }

    public HobbySubCategory createCategory(String categoryName, String categoryId) throws EntityNotFoundException {
        log.info("inside sub category business logic");
        // retrieve the parent category
        HobbyCategory parentCategory = hobbyCategoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException("Parent hobby with id " + categoryId + " not found"));
        log.info("found parent category with name {}", parentCategory.getCategoryName());
        HobbySubCategory newSubCategory = new HobbySubCategory();
        newSubCategory.setCategoryId(this.generateRandomUUID());
        newSubCategory.setCategoryName(this.convertNameToTitleCase(categoryName));
        newSubCategory.setHobbyCategory(parentCategory);
        newSubCategory.setLastEdited(this.generateLastEditedDate());

        // add this sub category to the list under the parent
        parentCategory.getSubCategory().add(newSubCategory);
        hobbyCategoryRepository.save(parentCategory);
        return newSubCategory;
    }

    @Override
    public HobbySubCategory updateCategory(String categoryName, String categoryId, MultipartFile iconImage) {
        return null;
    }

    public HobbySubCategory updateCategory(String categoryName, String categoryId) throws EntityNotFoundException {
        // retrieve the sub category from the DB
        HobbySubCategory subCategory = hobbySubCategoryRepository.findById(categoryId).orElseThrow(
            () -> new EntityNotFoundException("Subcategory with id " + categoryId + " not found"));

        subCategory.setCategoryName(this.convertNameToTitleCase(categoryName));
        subCategory.setLastEdited(this.generateLastEditedDate());

        return hobbySubCategoryRepository.save(subCategory);
    }

    @Override
    public void deleteCategory(String categoryId) {
        HobbySubCategory existingSubCategory = hobbySubCategoryRepository.findById(categoryId).orElseThrow(
            () -> new EntityNotFoundException("Sub category with id: " + categoryId + " not found"));
        hobbySubCategoryRepository.delete(existingSubCategory);
    }

    @Override
    public List<HobbySubCategory> getPagedCategoryList(Integer page, Integer size) {
        return null;
    }

    public List<HobbySubCategory> getPagedCategory(String parentCategoryId) throws EntityNotFoundException {
        HobbyCategory parentCategory = hobbyCategoryRepository.findById(parentCategoryId).orElseThrow(
            () -> new EntityNotFoundException("Parent category with id " + parentCategoryId + " not found"));

        return hobbySubCategoryRepository.findAllByHobbyCategory(parentCategory);
    }

    @Override
    public HobbySubCategory findCategoryById(String categoryId) throws EntityNotFoundException {
        return hobbySubCategoryRepository.findById(categoryId).orElseThrow(
            () -> new EntityNotFoundException("Sub category with id: " + categoryId + " not found"));
    }

    @Override
    public List<HobbySubCategory> searchCategoryByName(String name, Integer page, Integer size) {
        Pageable pageInfo = PageRequest.of(page, size);
        return hobbySubCategoryRepository.findByCategoryName(name, pageInfo).getContent();
    }
}
