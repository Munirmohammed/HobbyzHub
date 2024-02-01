package com.hobbyzhub.javabackend.followersmodule.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Represents the response for follow/unfollow operations.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class FollowResponse implements Serializable {
    // No need to include specific fields here since data is null when not needed
}
