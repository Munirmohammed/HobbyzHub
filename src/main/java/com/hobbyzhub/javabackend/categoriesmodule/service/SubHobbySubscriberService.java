package com.hobbyzhub.javabackend.categoriesmodule.service;

import com.hobbyzhub.javabackend.categoriesmodule.abstractions.AbstractEntityHelperDef;
import com.hobbyzhub.javabackend.categoriesmodule.abstractions.SubCategorySubscriptionServiceDef;
import com.hobbyzhub.javabackend.categoriesmodule.entity.HobbySubCategory;
import com.hobbyzhub.javabackend.categoriesmodule.entity.SubHobbySubscriber;
import com.hobbyzhub.javabackend.categoriesmodule.repository.HobbySubCategoryRepository;
import com.hobbyzhub.javabackend.categoriesmodule.repository.SubHobbySubscriberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubHobbySubscriberService extends AbstractEntityHelperDef<SubHobbySubscriber> implements SubCategorySubscriptionServiceDef {
    @Autowired
    private SubHobbySubscriberRepository subHobbySubscriberRepository;

    @Autowired
    private HobbySubCategoryRepository hobbySubCategoryRepository;

    @Override
    public void subscribeToSubCategory(SubHobbySubscriber subHobbySubscriber, String categoryId) {
        HobbySubCategory subCategory = hobbySubCategoryRepository.findById(categoryId).orElseThrow(
            () -> new EntityNotFoundException("Sub category with ID: " + categoryId + " not found"));

        if(!subHobbySubscriberRepository.existsByUserIdAndSubCategoryId(subHobbySubscriber.getUserId(), categoryId)) {
            subHobbySubscriber.setSubCategoryId(subCategory.getCategoryId());
            subHobbySubscriberRepository.save(subHobbySubscriber);
        }
    }

    @Override
    public void unsubscribeFromSubCategory(String categoryId, String userId) {
        subHobbySubscriberRepository.deleteByUserIdAndSubCategoryId(userId, categoryId);
    }

    @Override
    public List<HobbySubCategory> listOutUserSubscriptions(String userId, Integer page, Integer size) {
        Pageable pageInfo = PageRequest.of(page, size);
        return subHobbySubscriberRepository.findByUserId(userId, pageInfo).stream().parallel().map(
            e -> hobbySubCategoryRepository.findById(e.getSubCategoryId()).get()).collect(Collectors.toList());
    }

    @Override
    public List<SubHobbySubscriber> listOutSubHobbySubscribers(String categoryId, Integer page, Integer size) {
        Pageable pageInfo = PageRequest.of(page, size);
        return subHobbySubscriberRepository.findBySubCategoryId(categoryId, pageInfo).toList();
    }
}
