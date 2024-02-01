package com.hobbyzhub.javabackend.categoriesmodule.payload.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CRUHobbyCategoryResponse {
    private String categoryId;
    private String categoryName;
    private String lastEdited;
    private String iconLink;

}
