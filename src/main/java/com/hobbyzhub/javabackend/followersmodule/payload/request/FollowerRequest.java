package com.hobbyzhub.javabackend.followersmodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Munir Mohammed
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowerRequest {
    private String fromUserId;
    private String toUserId;
    private Integer page;
    private Integer size;
}
