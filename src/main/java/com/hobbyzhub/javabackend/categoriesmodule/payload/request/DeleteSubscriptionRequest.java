package com.hobbyzhub.javabackend.categoriesmodule.payload.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteSubscriptionRequest {
    private String userId;
    private String subCatId;
}
