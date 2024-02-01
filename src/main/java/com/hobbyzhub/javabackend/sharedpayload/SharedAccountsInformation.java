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
<<<<<<< HEAD
    private boolean categoryStatus;
=======
>>>>>>> origin/dev
    private String joinedDate;
    private String birthdate;
}
