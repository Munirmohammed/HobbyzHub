package com.hobbyzhub.javabackend.followersmodule.service.Impl;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.hobbyzhub.javabackend.followersmodule.entity.UserRelationship;
import com.hobbyzhub.javabackend.followersmodule.payload.request.UserRelationshipRequest;
import com.hobbyzhub.javabackend.followersmodule.payload.response.GenericServiceResponse;
import com.hobbyzhub.javabackend.followersmodule.payload.response.UserFollowerResponse;
import com.hobbyzhub.javabackend.followersmodule.payload.response.UserPreviewResponse;
import com.hobbyzhub.javabackend.followersmodule.repository.UserRelationshipRepository;
import com.hobbyzhub.javabackend.followersmodule.service.UserRelationshipService;
import com.hobbyzhub.javabackend.notificationsmodule.dto.MessageDTO;
import com.hobbyzhub.javabackend.notificationsmodule.service.FcmService;
import com.hobbyzhub.javabackend.securitymodule.SharedAccounts;
import com.hobbyzhub.javabackend.sharedpayload.SharedAccountsInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: Munir Mohammed
 */

@Service
public class UserRelationshipServiceImpl implements UserRelationshipService {

    private final Logger logger = LoggerFactory.getLogger(UserRelationshipServiceImpl.class);

    @Autowired
    private UserRelationshipRepository userRelationshipRepository;

    @Autowired
    private SharedAccounts accountManagementServiceFeign;

    @Autowired
    private FcmService fcmService;

//    @Autowired
//    private NotificationQueueService notificationQueueService;

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @Override
    public GenericServiceResponse<List<UserPreviewResponse>> getFollowings(UserRelationshipRequest userRelationshipRequest) {
        String myUserId = userRelationshipRequest.getMyUserId();
        logger.info("myUserId: {}", myUserId);
        // Set default values for page and size if not provided
        int page = userRelationshipRequest.getPage() != null ? userRelationshipRequest.getPage() : 0;
        int size = userRelationshipRequest.getSize() != null ? userRelationshipRequest.getSize() : 100;

        // Implement the logic to retrieve followings based on userRelationshipRequest, page, and size
        Page<UserRelationship> userRelationships = userRelationshipRepository.findByMyUserId(myUserId, PageRequest.of(page, size));
        logger.info("result: {}", userRelationships);
        // Fetch user details for each following using Feign client
        List<UserPreviewResponse> followings = userRelationships.stream()
                .peek(relationship -> {
                    // Log each other user Id before sending to get Data
                    logger.info("Fetching data for otherUserId: {}", relationship.getOtherUserId());
                })
                .map(relationship -> accountManagementServiceFeign.getData(relationship.getOtherUserId()))
                .collect(Collectors.toList());

        return new GenericServiceResponse<>(
                apiVersion,
                organizationName,
                "Successfully retrieved followings list",
                true,
                HttpStatus.OK.value(),
                followings);
    }

    @Override
    public GenericServiceResponse<UserPreviewResponse> followUnfollowUser(UserRelationshipRequest userRelationshipRequest) {
        String myUserId = userRelationshipRequest.getMyUserId();
        String otherUserId = userRelationshipRequest.getOtherUserId();
        logger.info("Attempting to follow/unfollow users. MyUserId: {}, OtherUserId: {}", myUserId, otherUserId);
        // Check if the relationship already exists
        UserRelationship existingRelationship = userRelationshipRepository.findByMyUserIdAndOtherUserId(myUserId, otherUserId);

        if (existingRelationship != null) {
            // if Relationship exists, delete (unfollow)
            userRelationshipRepository.delete(existingRelationship);

            return new GenericServiceResponse<>(
                    apiVersion,
                    organizationName,
                    "Successfully unfollowed user",
                    true,
                    HttpStatus.OK.value(),
                    null);
        } else {
            // if Relationship doesn't exist, create (follow)
            UserRelationship newRelationship = UserRelationship.builder()
                    .myUserId(myUserId)
                    .otherUserId(otherUserId)
                    .isFollowing(true)
                    .build();

            userRelationshipRepository.save(newRelationship);

            SharedAccountsInformation sharedInfo = accountManagementServiceFeign.retrieveSharedAccount(otherUserId);
            UserPreviewResponse userPreview = convertToUserPreviewResponse(sharedInfo);

            // Sending notification to the user being followed
            try {
                Map<String, String> data = new HashMap<>();
                data.put("followerId", userRelationshipRequest.getMyUserId()); // Assuming this is the key for the follower ID
                MessageDTO notificationMessage = new MessageDTO("You have a new follower",
                        "You have been followed by " + userPreview.getFullName(), data, "", userPreview.getFirebaseToken());
                System.out.println(notificationMessage);
                System.out.println(userPreview.getFirebaseToken());

                // Check if the Firebase token is not null before sending the notification
                if (userPreview.getFirebaseToken() != null) {
                    // Send the notification if the token is available
                    fcmService.sendNotificationToSpecificDevice(notificationMessage, userPreview.getFirebaseToken());
                    System.out.println(notificationMessage);
                    System.out.println(userPreview.getFirebaseToken());
                } else {
                    // If the Firebase token is null, log a message or handle it as needed
                    logger.warn("Firebase token is null for user: {}", userPreview.getUserId());
                    // notificationQueueService.queueNotification(new Notification(userPreview.getUserId(), notificationMessage));
                }
            } catch (FirebaseMessagingException e) {
                logger.error("Error sending notification to user: {}", userPreview.getUserId(), e);

            }






            return new GenericServiceResponse<>(
                    apiVersion,
                    organizationName,
                    "Successfully followed user",
                    true,
                    HttpStatus.CREATED.value(),
                    userPreview);
        }
    }

    private UserPreviewResponse convertToUserPreviewResponse(SharedAccountsInformation sharedInfo) {
        UserPreviewResponse userResponse = new UserPreviewResponse();
        userResponse.setUserId(sharedInfo.getUserId());
        userResponse.setFullName(sharedInfo.getFullName());
        userResponse.setProfileImage(sharedInfo.getProfileImage());
        userResponse.setFirebaseToken(sharedInfo.getFirebaseToken());
        return userResponse;
    }

    @Override
    public boolean isFollowing(UserRelationshipRequest userRelationshipRequest) {
        String myUserId = userRelationshipRequest.getMyUserId();
        String otherUserId = userRelationshipRequest.getOtherUserId();

        UserRelationship existingRelationship = userRelationshipRepository.findByMyUserIdAndOtherUserId(myUserId, otherUserId);

        return existingRelationship != null && existingRelationship.isFollowing();
    }

    @Override
    public GenericServiceResponse<Integer> getFollowersCount(String userId) {
        logger.info(userId);
        try {
            int followersCount = userRelationshipRepository.countByOtherUserId(userId);
            return new GenericServiceResponse<>(
                    apiVersion,
                    organizationName,
                    "Successfully retrieved followers count",
                    true,
                    HttpStatus.OK.value(),
                    followersCount);
        } catch (Exception ex) {
            logger.error("Error retrieving followers count for userId: {}", userId, ex);
            return new GenericServiceResponse<>(
                    apiVersion,
                    organizationName,
                    "Error retrieving followers count",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);
        }
    }

    @Override
    public GenericServiceResponse<Integer> getFollowingsCount(String userId) {
        try {
            logger.info(userId);
            int followingsCount = userRelationshipRepository.countByMyUserId(userId);
            return new GenericServiceResponse<>(
                    apiVersion,
                    organizationName,
                    "Successfully retrieved followings count",
                    true,
                    HttpStatus.OK.value(),
                    followingsCount);
        } catch (Exception ex) {
            logger.error("Error retrieving followings count for userId: {}", userId, ex);
            return new GenericServiceResponse<>(
                    apiVersion,
                    organizationName,
                    "Error retrieving followings count",
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);
        }
    }

    @Override
    public GenericServiceResponse<List<UserFollowerResponse>> getFollowersForUser(UserRelationshipRequest userRelationshipRequest) {
        String otherUserId = userRelationshipRequest.getOtherUserId();
        String myUserId = userRelationshipRequest.getMyUserId();

        int page = userRelationshipRequest.getPage() != null ? userRelationshipRequest.getPage() : 0;
        int size = userRelationshipRequest.getSize() != null ? userRelationshipRequest.getSize() : 100;

        // Implement the logic to retrieve followers for the specified user (myUserId)
        Page<UserRelationship> followerRelationships = userRelationshipRepository.findByOtherUserIdAndIsFollowing(
                otherUserId, true, PageRequest.of( page, size));

        List<UserFollowerResponse> followers = followerRelationships.getContent().stream()
                .filter(relationship -> !myUserId.equals(relationship.getMyUserId())) // Exclude myUserId
                .map(relationship -> {
                    UserPreviewResponse followerData = accountManagementServiceFeign.getData(relationship.getMyUserId());
                    boolean isFollowing = userRelationshipRepository.existsByMyUserIdAndOtherUserId(
                            myUserId, relationship.getMyUserId());

                    logger.info("Checking relationship: myUserId={}, followerUserId={}, isFollowing={}", myUserId, relationship.getMyUserId(), isFollowing);

                    return convertToUserFollowerResponse(followerData, isFollowing);
                })
                .collect(Collectors.toList());

        return new GenericServiceResponse<>(
                apiVersion,
                organizationName,
                "Successfully retrieved followers for the specified user",
                true,
                HttpStatus.OK.value(),
                followers);
    }

    @Override
    public GenericServiceResponse<List<UserFollowerResponse>> getFollowingsForUser(UserRelationshipRequest userRelationshipRequest) {
        String myUserId = userRelationshipRequest.getOtherUserId();
        String otherUserId = userRelationshipRequest.getMyUserId();

        int page = userRelationshipRequest.getPage() != null ? userRelationshipRequest.getPage() : 0;
        int size = userRelationshipRequest.getSize() != null ? userRelationshipRequest.getSize() : 100;

        // Implement the logic to retrieve followings for the specified user (myUserId)
        Page<UserRelationship> followingRelationships = userRelationshipRepository.findByMyUserId(
                myUserId,  PageRequest.of(page, size));

        List<UserFollowerResponse> followings = followingRelationships.getContent().stream()
                .filter(relationship -> !otherUserId.equals(relationship.getOtherUserId())) // Exclude myUserId
                .map(relationship -> {
                    UserPreviewResponse followingData = accountManagementServiceFeign.getData(relationship.getOtherUserId());
                    boolean isFollowing = userRelationshipRepository.existsByMyUserIdAndOtherUserId(
                            otherUserId, relationship.getOtherUserId());

                    logger.info("Checking relationship: myUserId={}, followingUserId={}, isFollowing={}", otherUserId, relationship.getOtherUserId(), isFollowing);

                    return convertToUserFollowerResponse(followingData, isFollowing);
                })
                .collect(Collectors.toList());

        return new GenericServiceResponse<>(
                apiVersion,
                organizationName,
                "Successfully retrieved followings for the specified user",
                true,
                HttpStatus.OK.value(),
                followings);
    }

    @Override
    public void deleteAllFollowersAndFollowings(String userId) {
        // Implement the logic to delete all followers and followings for a given user (userId)
        List<UserRelationship> relationships = userRelationshipRepository.findByMyUserIdOrOtherUserId(userId, userId);
        userRelationshipRepository.deleteAll(relationships);
    }

    @Override
    public GenericServiceResponse<List<UserFollowerResponse>> getFollowers(UserRelationshipRequest userRelationshipRequest) {
        String myUserId = userRelationshipRequest.getOtherUserId();
        int page = userRelationshipRequest.getPage() != null ? userRelationshipRequest.getPage() : 0;
        int size = userRelationshipRequest.getSize() != null ? userRelationshipRequest.getSize() : 100;

        Page<UserRelationship> followerRelationships = userRelationshipRepository.findByOtherUserIdAndIsFollowing(
                myUserId, true, PageRequest.of(page, size));

        List<UserFollowerResponse> followers = followerRelationships.getContent().stream()
                .map(relationship -> {
                    UserPreviewResponse followerData = accountManagementServiceFeign.getData(relationship.getMyUserId());
                    boolean isFollowing = userRelationshipRepository.existsByMyUserIdAndOtherUserId(
                            relationship.getOtherUserId(), relationship.getMyUserId());
                    return convertToUserFollowerResponse(followerData, isFollowing);
                })
                .collect(Collectors.toList());

        return new GenericServiceResponse<>(
                apiVersion,
                organizationName,
                "Successfully retrieved followers list",
                true,
                HttpStatus.OK.value(),
                followers);
    }


    private UserFollowerResponse convertToUserFollowerResponse(UserPreviewResponse userPreview, boolean isFollowing) {
        return UserFollowerResponse.builder()
                .userId(userPreview.getUserId())
                .fullName(userPreview.getFullName())
                .profileImage(userPreview.getProfileImage())
                .following(isFollowing)
                .build();
    }
}
