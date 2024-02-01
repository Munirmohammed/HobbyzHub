package com.hobbyzhub.javabackend.categoriesmodule.payload.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDeleteSubscriptionRequest {
    private String userId;
    private String userName;
    private String profilePicLink;
    private String subCategoryId;
}
