package com.hobbyzhub.javabackend.categoriesmodule.payload.request;

import lombok.Data;

/**
 *
 * This class represents both the request body structure for getting subscription info.
 * Only one of the fields is provided at a time.
 * When the userId is provided, we will return the sub categories the user is subscribed to.
 * When the subsCategoryId is provided, we will return the users subscribed to it.
 * If both are provided at the same time, only one will be considered by the logic
 */
@Data
public class ReadSubscriptionsRequest {
    private String userId;
    private String subCategoryId;
    private Integer page;
    private Integer size;
}
