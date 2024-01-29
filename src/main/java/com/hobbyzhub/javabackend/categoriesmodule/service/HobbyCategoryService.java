package com.hobbyzhub.javabackend.categoriesmodule.service;

import com.hobbyzhub.javabackend.categoriesmodule.abstractions.AbstractEntityHelperDef;
import com.hobbyzhub.javabackend.categoriesmodule.abstractions.CategoryServiceDef;
import com.hobbyzhub.javabackend.categoriesmodule.entity.HobbyCategory;
import com.hobbyzhub.javabackend.categoriesmodule.repository.HobbyCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class HobbyCategoryService extends AbstractEntityHelperDef<HobbyCategory> implements CategoryServiceDef<HobbyCategory> {
    @Autowired
    private HobbyCategoryRepository hobbyCategoryRepository;

    @Override
    public HobbyCategory createCategory(String categoryName, MultipartFile iconImage, String categoryId) {
        String iconLink = "";

        HobbyCategory newHobbyCategory = new HobbyCategory();
        newHobbyCategory.setCategoryId(this.generateRandomUUID());
        newHobbyCategory.setCategoryName(this.convertNameToTitleCase(categoryName));
        newHobbyCategory.setLastEdited(this.generateLastEditedDate());

        // upload the iconImage so that we can get an iconLink
        if(Objects.nonNull(iconImage)) {
            iconLink = this.generateFileUrl(iconImage);
            newHobbyCategory.setIconLink(iconLink);
        }

        return hobbyCategoryRepository.save(newHobbyCategory);
    }

    @Override
    public HobbyCategory updateCategory(String categoryName, String categoryId, MultipartFile iconImage) {
        String iconLink = "";
        HobbyCategory existingCategory = hobbyCategoryRepository.findById(categoryId).orElseThrow(
            () -> new EntityNotFoundException("Hobby category with id " + categoryId + " not found"));

        // update the profile picture if a new one is provided
        if(Objects.nonNull(iconImage)) {
            iconLink = this.generateFileUrl(iconImage);
            existingCategory.setIconLink(iconLink);
        }

        // update other details and save the updated Hobby Category
        existingCategory.setCategoryName(this.convertNameToTitleCase(categoryName));
        existingCategory.setLastEdited(this.generateLastEditedDate());
        return hobbyCategoryRepository.save(existingCategory);
    }


    @Override
    public void deleteCategory(String categoryId) throws EntityNotFoundException {
        HobbyCategory existingCategory = hobbyCategoryRepository.findById(categoryId).orElseThrow(
            () -> new EntityNotFoundException("Category with id: " + categoryId + " not found"));
        hobbyCategoryRepository.delete(existingCategory);
    }

    @Override
    public List<HobbyCategory> getPagedCategoryList(Integer page, Integer size) {
        return this.retrievePagedList(page, size, HobbyCategory.class);
    }

    @Override
    public HobbyCategory findCategoryById(String categoryId) throws EntityNotFoundException {
        return hobbyCategoryRepository.findById(categoryId).orElseThrow(
            () -> new EntityNotFoundException("Category with id: " + categoryId + " not found"));
    }

    @Override
    public List<HobbyCategory> searchCategoryByName(String name, Integer page, Integer size) {
        Pageable pageInfo = PageRequest.of(page, size);
        return hobbyCategoryRepository.findByCategoryName(name, pageInfo).stream().toList();
    }
}
