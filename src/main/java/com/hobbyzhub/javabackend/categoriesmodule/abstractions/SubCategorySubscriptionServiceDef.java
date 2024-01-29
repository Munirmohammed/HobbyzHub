package com.hobbyzhub.javabackend.categoriesmodule.abstractions;

import com.hobbyzhub.javabackend.categoriesmodule.entity.HobbySubCategory;
import com.hobbyzhub.javabackend.categoriesmodule.entity.SubHobbySubscriber;

import java.util.List;

public interface SubCategorySubscriptionServiceDef {
    void subscribeToSubCategory(SubHobbySubscriber subHobbySubscriber, String categoryId);
    void unsubscribeFromSubCategory(String categoryId, String userId);
    List<HobbySubCategory> listOutUserSubscriptions(String userId, Integer page, Integer size);
    List<SubHobbySubscriber> listOutSubHobbySubscribers(String categoryId, Integer page, Integer size);
}
