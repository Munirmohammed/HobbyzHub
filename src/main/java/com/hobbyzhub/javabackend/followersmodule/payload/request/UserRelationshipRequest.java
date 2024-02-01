package com.hobbyzhub.javabackend.followersmodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRelationshipRequest {
    private String myUserId;
    private String otherUserId;
    private Integer page;
    private Integer size;
}
