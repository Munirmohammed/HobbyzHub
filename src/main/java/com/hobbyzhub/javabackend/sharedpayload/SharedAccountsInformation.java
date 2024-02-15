package com.hobbyzhub.javabackend.sharedpayload;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SharedAccountsInformation {
    private String email;
    private String userId;
    private String fullName;
    private String gender;
    private String bio;
    private String profileImage;
    private boolean categoryStatus;
    private String joinedDate;
    private String birthdate;
    private String firebaseToken;
}
