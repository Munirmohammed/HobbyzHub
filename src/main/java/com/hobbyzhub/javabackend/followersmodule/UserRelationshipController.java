package com.hobbyzhub.javabackend.followersmodule;

import com.hobbyzhub.javabackend.followersmodule.payload.request.UserRelationshipRequest;
import com.hobbyzhub.javabackend.followersmodule.payload.response.GenericServiceResponse;
import com.hobbyzhub.javabackend.followersmodule.payload.response.UserPreviewResponse;
import com.hobbyzhub.javabackend.followersmodule.service.UserRelationshipService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Munir Mohammed
 */

@RestController
@RequestMapping(value = "api/v1/user-relationship")
public class UserRelationshipController {

    @Autowired
    private UserRelationshipService userRelationshipService;

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @PostMapping(value = "/follow-unfollow")
    public ResponseEntity<GenericServiceResponse<UserPreviewResponse>> followUnfollowUser(
            @RequestBody UserRelationshipRequest userRelationshipRequest) {
        GenericServiceResponse<UserPreviewResponse> response = userRelationshipService.followUnfollowUser(userRelationshipRequest);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @PostMapping(value = "/check-following")
    public ResponseEntity<GenericServiceResponse<FollowingStatus>> checkFollowing(
            @RequestBody UserRelationshipRequest userRelationshipRequest) {
        boolean isFollowing = userRelationshipService.isFollowing(userRelationshipRequest);

        FollowingStatus followingStatus = new FollowingStatus(isFollowing);
        String message = isFollowing ? "User is following" : "User is not following";

        GenericServiceResponse<FollowingStatus> response = new GenericServiceResponse<>(
                apiVersion,
                organizationName,
                message,
                true,
                HttpStatus.OK.value(),
                followingStatus);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Data
    public static class FollowingStatus {
        private final boolean isFollowing;
    }

}
