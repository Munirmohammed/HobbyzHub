package com.hobbyzhub.javabackend.followersmodule;

import com.hobbyzhub.javabackend.followersmodule.payload.request.CountRequest;
import com.hobbyzhub.javabackend.followersmodule.payload.request.UserRelationshipRequest;
import com.hobbyzhub.javabackend.followersmodule.payload.response.GenericServiceResponse;
import com.hobbyzhub.javabackend.followersmodule.payload.response.UserFollowerCountResponse;
import com.hobbyzhub.javabackend.followersmodule.payload.response.UserFollowerResponse;
import com.hobbyzhub.javabackend.followersmodule.service.UserRelationshipService;
import com.hobbyzhub.javabackend.securitymodule.SharedAccounts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Munir Mohammed
 */

@RestController
@RequestMapping(value = "api/v1/follower")
@Slf4j
public class UserFollowersController {

    @Autowired
    private UserRelationshipService userRelationshipService;

    @Autowired
    private SharedAccounts postFeign;

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @PostMapping(value = "/get")
    public ResponseEntity<GenericServiceResponse<List<UserFollowerResponse>>> getFollowerMap(
            @RequestBody UserRelationshipRequest userRelationshipRequest) {
        try {
            String toUserId = userRelationshipRequest.getOtherUserId();

            // Paged list of followers
            GenericServiceResponse<List<UserFollowerResponse>> followersResponse =
                    userRelationshipService.getFollowers(userRelationshipRequest);

            if (!followersResponse.getSuccess()) {
                // If the service call was not successful, propagate the error response
                return new ResponseEntity<>(
                        new GenericServiceResponse<>(
                                apiVersion,
                                organizationName,
                                followersResponse.getMessage(),
                                false,
                                followersResponse.getStatus(),
                                null),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

            List<UserFollowerResponse> followers = followersResponse.getData();

            if (followers.isEmpty()) {
                GenericServiceResponse<List<UserFollowerResponse>> noContentResponse =
                        new GenericServiceResponse<>(
                                apiVersion,
                                organizationName,
                                "No followers found for the specified user",
                                false,
                                HttpStatus.NOT_FOUND.value(),
                                null);

                return new ResponseEntity<>(noContentResponse, HttpStatus.NOT_FOUND);
            }

            List<UserFollowerResponse> resourcesList = new ArrayList<>();

            // Iterate through each follower and make individual calls
            for (UserFollowerResponse follower : followers) {
                boolean isFollowing = userRelationshipService.isFollowing(
                        UserRelationshipRequest.builder()
                                .myUserId(toUserId)
                                .otherUserId(follower.getUserId())
                                .build());
                UserFollowerResponse userFollower = convertToUserFollowerResponse(follower, isFollowing);
                resourcesList.add(userFollower);
            }

            GenericServiceResponse<List<UserFollowerResponse>> genericResponse =
                    new GenericServiceResponse<>(
                            apiVersion,
                            organizationName,
                            "Successfully retrieved followers list",
                            true,
                            HttpStatus.OK.value(),
                            resourcesList);

            log.info(genericResponse.toString());

            return new ResponseEntity<>(genericResponse, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error populating followers list for userId", ex);
            GenericServiceResponse<List<UserFollowerResponse>> errorResponse =
                    new GenericServiceResponse<>(
                            apiVersion,
                            organizationName,
                            "Error populating followers list",
                            false,
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            null);

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private UserFollowerResponse convertToUserFollowerResponse(UserFollowerResponse userPreview, boolean isFollowing) {
        return UserFollowerResponse.builder()
                .userId(userPreview.getUserId())
                .fullName(userPreview.getFullName())
                .profileImage(userPreview.getProfileImage())
                .following(isFollowing)
                .build();
    }

    @PostMapping(value = "/get-third")
    public ResponseEntity<GenericServiceResponse<List<UserFollowerResponse>>> getFollowersOfThirdPerson(
            @RequestBody UserRelationshipRequest userRelationshipRequest) {
        try {
            String myUserId = userRelationshipRequest.getMyUserId();
            // Get followers of the third person
            GenericServiceResponse<List<UserFollowerResponse>> followersResponse =
                    userRelationshipService.getFollowersForUser(userRelationshipRequest);

            if (!followersResponse.getSuccess()) {
                // If the service call was not successful, propagate the error response
                return new ResponseEntity<>(
                        new GenericServiceResponse<>(
                                apiVersion,
                                organizationName,
                                followersResponse.getMessage(),
                                false,
                                followersResponse.getStatus(),
                                null),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

            List<UserFollowerResponse> followers = followersResponse.getData();

            if (followers.isEmpty()) {
                GenericServiceResponse<List<UserFollowerResponse>> noContentResponse =
                        new GenericServiceResponse<>(
                                apiVersion,
                                organizationName,
                                "No followers found for the specified user",
                                false,
                                HttpStatus.NOT_FOUND.value(),
                                null);

                return new ResponseEntity<>(noContentResponse, HttpStatus.NOT_FOUND);
            }

            List<UserFollowerResponse> resourcesList = new ArrayList<>();

            // Iterate through each follower and set the 'following' field based on their individual isFollowing value
            for (UserFollowerResponse follower : followers) {
                boolean isFollowing = userRelationshipService.isFollowing(
                        UserRelationshipRequest.builder()
                                .myUserId(myUserId)
                                .otherUserId(follower.getUserId())
                                .build());

                UserFollowerResponse userFollower = convertToUserFollowerResponse(follower, isFollowing);
                resourcesList.add(userFollower);
            }

            GenericServiceResponse<List<UserFollowerResponse>> genericResponse =
                    new GenericServiceResponse<>(
                            apiVersion,
                            organizationName,
                            "Successfully retrieved followers list for the third person",
                            true,
                            HttpStatus.OK.value(),
                            resourcesList);

            log.info(genericResponse.toString());

            return new ResponseEntity<>(genericResponse, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error retrieving followers list for the third person", ex);
            GenericServiceResponse<List<UserFollowerResponse>> errorResponse =
                    new GenericServiceResponse<>(
                            apiVersion,
                            organizationName,
                            "Error retrieving followers list for the third person",
                            false,
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            null);

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/count")
    public ResponseEntity<GenericServiceResponse<UserFollowerCountResponse>> getUserRelationshipCount(
            @RequestBody CountRequest countRequest) {
        try {
            String userId = countRequest.getUserID();
            log.info(userId);

            GenericServiceResponse<Integer> followersCountResponse = userRelationshipService.getFollowersCount(userId);
            GenericServiceResponse<Integer> followingsCountResponse = userRelationshipService.getFollowingsCount(userId);

            GenericServiceResponse<UserFollowerCountResponse> genericResponse = getUserFollowerCountResponseGenericServiceResponse(followersCountResponse, followingsCountResponse, userId);

            log.info(genericResponse.toString());

            return new ResponseEntity<>(genericResponse, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error retrieving user relationship counts", ex);
            GenericServiceResponse<UserFollowerCountResponse> errorResponse =
                    new GenericServiceResponse<>(
                            apiVersion,
                            organizationName,
                            "Error retrieving user relationship counts",
                            false,
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            null);

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private GenericServiceResponse<UserFollowerCountResponse> getUserFollowerCountResponseGenericServiceResponse(GenericServiceResponse<Integer> followersCountResponse, GenericServiceResponse<Integer> followingsCountResponse, String userId) {
        Integer followersCount = followersCountResponse.getData();
        Integer followingsCount = followingsCountResponse.getData();

        // Fetch post count using Feign
        ResponseEntity<?> postCountResponseEntity = postFeign.getPostCount(userId);
        int postCount = (int) postCountResponseEntity.getBody();

        UserFollowerCountResponse userCountResponse = new UserFollowerCountResponse(userId, followersCount, followingsCount, postCount);

        if (followersCount == 0 && followingsCount == 0 && postCount == 0) {
            // Return an error response if all counts are zero
            return new GenericServiceResponse<>(
                    apiVersion,
                    organizationName,
                    "No followers, followings, and posts found for the user",
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    null);
        }

        GenericServiceResponse<UserFollowerCountResponse> genericResponse =
                new GenericServiceResponse<>(
                        apiVersion,
                        organizationName,
                        "Successfully retrieved user relationship counts",
                        true,
                        HttpStatus.OK.value(),
                        userCountResponse);
        return genericResponse;
    }




}
