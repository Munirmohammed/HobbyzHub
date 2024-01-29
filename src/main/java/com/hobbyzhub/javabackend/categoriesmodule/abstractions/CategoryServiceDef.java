package com.hobbyzhub.javabackend.categoriesmodule.abstractions;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface CategoryServiceDef<T> {
    public T createCategory(String categoryName, MultipartFile iconImage, String categoryId);
    public T updateCategory(String categoryName, String categoryId, MultipartFile iconImage);
    public void deleteCategory(String categoryId);
    public List<T> getPagedCategoryList(Integer page, Integer size);
    public T findCategoryById(String categoryId);
    public List<T> searchCategoryByName(String name, Integer page, Integer size);
    public default LocalDate generateLastEditedDate() {
        // get the LocalDate now
        LocalDate dateNow = LocalDate.now();

        //create the pattern we want to use
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // format dd-MM-yyyy
        String formattedDate = dateNow.format(dateFormat);
        return LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
