package com.hobbyzhub.javabackend.followersmodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Munir Mohammed
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountRequest {
    private String userID;

}

