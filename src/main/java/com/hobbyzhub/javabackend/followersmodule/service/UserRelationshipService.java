package com.hobbyzhub.javabackend.followersmodule.service;

import com.hobbyzhub.javabackend.followersmodule.payload.request.UserRelationshipRequest;
import com.hobbyzhub.javabackend.followersmodule.payload.response.GenericServiceResponse;
import com.hobbyzhub.javabackend.followersmodule.payload.response.UserFollowerResponse;
import com.hobbyzhub.javabackend.followersmodule.payload.response.UserPreviewResponse;

import java.util.List;

public interface UserRelationshipService {
    GenericServiceResponse<UserPreviewResponse> followUnfollowUser(UserRelationshipRequest userRelationshipRequest);

    boolean isFollowing(UserRelationshipRequest userRelationshipRequest);

    GenericServiceResponse<List<UserPreviewResponse>> getFollowings(UserRelationshipRequest userRelationshipRequest);

    void deleteAllFollowersAndFollowings(String myUserId);

    GenericServiceResponse<List<UserFollowerResponse>> getFollowers(UserRelationshipRequest userRelationshipRequest);

    // New method to get followers of a third person
    GenericServiceResponse<List<UserFollowerResponse>> getFollowersForUser(UserRelationshipRequest userRelationshipRequest);

    // Added method to the UserRelationshipService interface for Followings
    GenericServiceResponse<List<UserFollowerResponse>> getFollowingsForUser(UserRelationshipRequest userRelationshipRequest);

    // New methods for counts
    GenericServiceResponse<Integer> getFollowersCount(String userId);

    GenericServiceResponse<Integer> getFollowingsCount(String userId);

}
