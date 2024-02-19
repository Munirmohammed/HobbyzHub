package com.hobbyzhub.javabackend.followersmodule.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @author Munir Mohammed
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPreviewResponse implements Serializable {
    private String userId;
    private String fullName;
    private String profileImage;
    private String firebaseToken;
}
