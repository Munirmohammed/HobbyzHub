package com.hobbyzhub.javabackend.followersmodule;

import com.hobbyzhub.javabackend.followersmodule.payload.request.UserRelationshipRequest;
import com.hobbyzhub.javabackend.followersmodule.payload.response.GenericServiceResponse;
import com.hobbyzhub.javabackend.followersmodule.payload.response.UserFollowerResponse;
import com.hobbyzhub.javabackend.followersmodule.payload.response.UserPreviewResponse;
import com.hobbyzhub.javabackend.followersmodule.service.UserRelationshipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: Munir Mohammed
 */

@RestController
@RequestMapping(value = "api/v1/following")
public class UserFollowingsController {

    @Autowired
    private UserRelationshipService userRelationshipService;

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    private final Logger logger = LoggerFactory.getLogger(UserFollowingsController.class);

    @PostMapping(value = "/get")
    public ResponseEntity<GenericServiceResponse<List<UserPreviewResponse>>> getFollowings(
            @RequestBody UserRelationshipRequest userRelationshipRequest) {
        try {
            // Paged list of followings
            GenericServiceResponse<List<UserPreviewResponse>> followingsResponse =
                    userRelationshipService.getFollowings(userRelationshipRequest);

            if (!followingsResponse.getSuccess()) {
                // If the service call was not successful, propagate the error response
                return new ResponseEntity<>(
                        new GenericServiceResponse<>(
                                apiVersion,
                                organizationName,
                                followingsResponse.getMessage(),
                                false,
                                followingsResponse.getStatus(),
                                null),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

            List<UserPreviewResponse> followings = followingsResponse.getData();
//            logger.debug("Followings List: {}", followings);

            if (followings.isEmpty()) {
                GenericServiceResponse<List<UserPreviewResponse>> noContentResponse =
                        new GenericServiceResponse<>(
                                apiVersion,
                                organizationName,
                                "No followings found for the specified user",
                                false,
                                HttpStatus.NOT_FOUND.value(),
                                null);

                return new ResponseEntity<>(noContentResponse, HttpStatus.NOT_FOUND);
            }

            GenericServiceResponse<List<UserPreviewResponse>> genericResponse =
                    new GenericServiceResponse<>(
                            apiVersion,
                            organizationName,
                            "Successfully retrieved followings list",
                            true,
                            HttpStatus.OK.value(),
                            followings);

            logger.info(genericResponse.toString());

            return new ResponseEntity<>(genericResponse, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error populating followings list for userId", ex);
            GenericServiceResponse<List<UserPreviewResponse>> errorResponse =
                    new GenericServiceResponse<>(
                            apiVersion,
                            organizationName,
                            "Error populating followings list",
                            false,
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            null);

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/unfollow-all/{fromUserId}")
    public ResponseEntity<GenericServiceResponse<Void>> deleteAllFollowingMap(@PathVariable String fromUserId) {
        try {
            userRelationshipService.deleteAllFollowersAndFollowings(fromUserId);

            GenericServiceResponse<Void> genericResponse =
                    new GenericServiceResponse<>(
                            apiVersion,
                            organizationName,
                            "Successfully unfollowed all users",
                            true,
                            HttpStatus.OK.value(),
                            null);

            return new ResponseEntity<>(genericResponse, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error unfollowing all users: {}", ex.getMessage());
            GenericServiceResponse<Void> errorResponse =
                    new GenericServiceResponse<>(
                            apiVersion,
                            organizationName,
                            "Error unfollowing all users",
                            false,
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            null);

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/get-third")
    public ResponseEntity<GenericServiceResponse<List<UserFollowerResponse>>> getFollowingsForUser(
            @RequestBody UserRelationshipRequest userRelationshipRequest) {
        try {
            // Paged list of followings
            GenericServiceResponse<List<UserFollowerResponse>> followingsResponse =
                    userRelationshipService.getFollowingsForUser(userRelationshipRequest);

            if (!followingsResponse.getSuccess()) {
                // If the service call was not successful, propagate the error response
                return new ResponseEntity<>(
                        new GenericServiceResponse<>(
                                apiVersion,
                                organizationName,
                                followingsResponse.getMessage(),
                                false,
                                followingsResponse.getStatus(),
                                null),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

            List<UserFollowerResponse> followings = followingsResponse.getData();

            if (followings.isEmpty()) {
                GenericServiceResponse<List<UserFollowerResponse>> noContentResponse =
                        new GenericServiceResponse<>(
                                apiVersion,
                                organizationName,
                                "No followings found for the specified user",
                                false,
                                HttpStatus.NOT_FOUND.value(),
                                null);

                return new ResponseEntity<>(noContentResponse, HttpStatus.NOT_FOUND);
            }

            GenericServiceResponse<List<UserFollowerResponse>> genericResponse =
                    new GenericServiceResponse<>(
                            apiVersion,
                            organizationName,
                            "Successfully retrieved followings list",
                            true,
                            HttpStatus.OK.value(),
                            followings);

            logger.info(genericResponse.toString());

            return new ResponseEntity<>(genericResponse, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("Error populating followings list for userId", ex);
            GenericServiceResponse<List<UserFollowerResponse>> errorResponse =
                    new GenericServiceResponse<>(
                            apiVersion,
                            organizationName,
                            "Error populating followings list",
                            false,
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            null);

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
