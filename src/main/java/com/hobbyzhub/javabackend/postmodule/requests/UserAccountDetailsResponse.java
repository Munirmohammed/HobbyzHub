package com.hobbyzhub.javabackend.postmodule.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author bikathi_martin
 */
@Getter
@Setter
@AllArgsConstructor
public class UserAccountDetailsResponse {
    private String fullName;
    private String email;
    private String profileImage;
    private String tokenExpiryTime;
}
