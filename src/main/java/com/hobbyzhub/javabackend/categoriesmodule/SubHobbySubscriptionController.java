package com.hobbyzhub.javabackend.categoriesmodule;

import com.hobbyzhub.javabackend.categoriesmodule.abstractions.AbstractSubscriptionControllerDef;
import com.hobbyzhub.javabackend.categoriesmodule.entity.HobbySubCategory;
import com.hobbyzhub.javabackend.categoriesmodule.entity.SubHobbySubscriber;
import com.hobbyzhub.javabackend.categoriesmodule.payload.request.CreateDeleteSubscriptionRequest;
import com.hobbyzhub.javabackend.categoriesmodule.payload.request.ReadSubscriptionsRequest;
import com.hobbyzhub.javabackend.categoriesmodule.payload.response.CRUHobbyCategoryResponse;
import com.hobbyzhub.javabackend.categoriesmodule.service.SubHobbySubscriberService;
import com.hobbyzhub.javabackend.securitymodule.SharedAccounts;
import com.hobbyzhub.javabackend.sharedexceptions.ServerErrorException;
import com.hobbyzhub.javabackend.sharedpayload.GenericResponse;
import com.hobbyzhub.javabackend.sharedpayload.SharedAccountsInformation;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/api/v1/categories/subscription")
public class SubHobbySubscriptionController extends AbstractSubscriptionControllerDef<SubHobbySubscriber> {
    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SubHobbySubscriberService subHobbySubscriberService;

    @Autowired
    private SharedAccounts sharedAccounts;


    @Override
    @PostMapping(value = "/subscribe", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> subscribeUserToSubCategory(@RequestBody CreateDeleteSubscriptionRequest request) {
        SubHobbySubscriber subscriber = this.mapPayloadToEntity(request);
        try {
            String userId = request.getUserId();
            log.info("Subscribing user with ID: {}", userId);
            SharedAccountsInformation userInformation = sharedAccounts.retrieveSharedAccount(userId);
            subHobbySubscriberService.subscribeToSubCategory(subscriber, request.getSubCategoryId());
            userInformation.setCategoryStatus(true);
            // Update user information
            sharedAccounts.updateUserInformation(userId, userInformation);
            subHobbySubscriberService.subscribeToSubCategory(subscriber, request.getSubCategoryId());
            return ResponseEntity.ok(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully subscribed to sub category",
                true,
                HttpStatus.OK.value(),
                    null
            ));
        } catch(EntityNotFoundException ex) {
            log.error("Attempt at subscribing to non-existent category");
            throw new ServerErrorException(ex.getMessage());
        }
    }

    @Override
    @DeleteMapping(value = "/unsubscribe", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> unsubscribeUserFromSubCategory(@RequestBody CreateDeleteSubscriptionRequest request) {
        try {
            subHobbySubscriberService.unsubscribeFromSubCategory(request.getSubCategoryId(), request.getUserId());
            return ResponseEntity.ok(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully unsubscribed from sub category",
                true,
                HttpStatus.OK.value(),
                null
            ));
        } catch (EntityNotFoundException ex) {
            log.error("Attempt at subscribing to non-existent category");
            throw new ServerErrorException(ex.getMessage());
        }
    }

    @Override
    @PostMapping(value = "/get-subscriptions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListOfUserSubscriptions(@RequestBody ReadSubscriptionsRequest request) {
        List<HobbySubCategory> subscriptions =
            subHobbySubscriberService.listOutUserSubscriptions(request.getUserId(), request.getPage(), request.getSize());
        if(subscriptions.isEmpty()) {
            return ResponseEntity.ok(new GenericResponse<>(
                apiVersion,
                organizationName,
                "User not subscribed to any sub categories",
                true,
                HttpStatus.OK.value(),
                null
            ));
        }

        List<CRUHobbyCategoryResponse> userSubscriptions = subscriptions.parallelStream().map(this::convertSubHobbyToResponseType).toList();
        return ResponseEntity.ok(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Found list of all user subscriptions",
            true,
            HttpStatus.OK.value(),
            userSubscriptions
        ));
    }

    @Override
    @PostMapping(value = "/get-subscribers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListOfSubCategorySubscribers(@RequestBody ReadSubscriptionsRequest request) {
        List<SubHobbySubscriber> subscribers = subHobbySubscriberService.listOutSubHobbySubscribers(
            request.getSubCategoryId(), request.getPage(), request.getSize());
        if(subscribers.isEmpty()) {
            return ResponseEntity.ok(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Sub categories has no subscribers",
                true,
                HttpStatus.OK.value(),
                null
            ));
        }
        return ResponseEntity.ok(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Found a paged list of sub category subscribers",
            true,
            HttpStatus.OK.value(),
            subscribers
        ));
    }

    private CRUHobbyCategoryResponse convertSubHobbyToResponseType(HobbySubCategory subCategory) {
        CRUHobbyCategoryResponse response = new CRUHobbyCategoryResponse();
        response.setCategoryId(subCategory.getCategoryId());
        response.setCategoryName(subCategory.getCategoryName());

        return response;
    }

    private SubHobbySubscriber mapPayloadToEntity(CreateDeleteSubscriptionRequest payload) {
        SubHobbySubscriber subscriber = new SubHobbySubscriber();
        subscriber.setSubCategoryId(payload.getSubCategoryId());
        subscriber.setUserId(payload.getUserId());
        subscriber.setProfilePicLink(payload.getProfilePicLink());
        subscriber.setUserName(payload.getUserName());
        return subscriber;
    }
}
