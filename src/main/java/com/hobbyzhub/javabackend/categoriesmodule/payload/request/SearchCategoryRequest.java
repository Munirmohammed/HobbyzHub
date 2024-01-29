package com.hobbyzhub.javabackend.categoriesmodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCategoryRequest {
    private String searchSlug;
    private Integer page;
    private Integer size;
}
