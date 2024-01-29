package com.hobbyzhub.javabackend.categoriesmodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateSubscriptionRequest {
    private String userId;
    private String userName;
    private String subCategoryId;
    private String profilePicLink;
}
