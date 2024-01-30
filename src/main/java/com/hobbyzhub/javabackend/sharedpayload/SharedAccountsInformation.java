package com.hobbyzhub.javabackend.sharedpayload;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SharedAccountsInformation {
    private String email;
    private String userId;
    private String fullName;
    private String gender;
    private String bio;
    private String profileImage;
    private String joinedDate;
    private String birthdate;
}
