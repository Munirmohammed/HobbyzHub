package com.hobbyzhub.javabackend.categoriesmodule.abstractions;

import com.hobbyzhub.javabackend.categoriesmodule.payload.request.CreateDeleteSubscriptionRequest;
import com.hobbyzhub.javabackend.categoriesmodule.payload.request.ReadSubscriptionsRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public abstract class AbstractSubscriptionControllerDef<T> {
    @Autowired
    private ModelMapper modelMapper;

    public abstract ResponseEntity<?> subscribeUserToSubCategory(CreateDeleteSubscriptionRequest request);
    public abstract ResponseEntity<?> unsubscribeUserFromSubCategory(CreateDeleteSubscriptionRequest request);
    public abstract ResponseEntity<?> getListOfUserSubscriptions(ReadSubscriptionsRequest request);
    public abstract ResponseEntity<?> getListOfSubCategorySubscribers(ReadSubscriptionsRequest request);

    protected final Object mapEntityToPayload(T entity, Class<?> clazz) {
        return modelMapper.map(entity, clazz);
    }
}
