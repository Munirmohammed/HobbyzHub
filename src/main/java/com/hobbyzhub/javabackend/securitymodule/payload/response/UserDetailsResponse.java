package com.hobbyzhub.javabackend.securitymodule.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponse {
    private String email;
    private String userId;
    private String fullName;
    private String gender;
    private String bio;
    private String profileImage;
    private String joinedDate;
    private String birthdate;

    public UserDetailsResponse(Object o) {
    }
}
