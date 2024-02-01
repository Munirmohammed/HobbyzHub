package com.hobbyzhub.javabackend.followersmodule.payload.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author Munir Mohammed
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowerCountResponse implements Serializable {
    private String userID;
    private Integer followersCount;
    private Integer followingsCount;
    private Integer postCount;
}
