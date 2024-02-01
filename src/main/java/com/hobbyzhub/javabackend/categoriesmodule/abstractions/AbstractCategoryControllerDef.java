package com.hobbyzhub.javabackend.categoriesmodule.abstractions;

import com.hobbyzhub.javabackend.categoriesmodule.payload.request.GetDeleteCategoryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

public abstract class AbstractCategoryControllerDef<T> {
    public abstract ResponseEntity<?> createCategory(MultipartFile iconImage, String categoryName, String parentCategoryId);
    public abstract ResponseEntity<?> updateCategory(MultipartFile iconImage, String categoryId, String categoryName);
    public abstract ResponseEntity<?> getCategory(GetDeleteCategoryRequest request);
    public abstract ResponseEntity<?> deleteHobbyCategory(@RequestBody GetDeleteCategoryRequest request);
}
