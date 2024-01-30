package com.hobbyzhub.javabackend.securitymodule;

import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.service.AppUserService;
import com.hobbyzhub.javabackend.securitymodule.util.def.EntityModelMapper;
import com.hobbyzhub.javabackend.sharedpayload.SharedAccountsInformation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class SharedAccounts {
    @Autowired
    private AppUserService appUserService;

    public SharedAccountsInformation retrieveSharedAccount(String userId) {
        AppUser appUser = appUserService.findUserById(userId);
        SharedAccountsInformation userDetails = SharedAccountsInformation.builder()
            .email(appUser.getEmail())
            .userId(appUser.getUserId())
            .fullName(appUser.getFullName())
            .bio(appUser.getBio())
            .profileImage(appUser.getProfileImage())
        .build();

        if(!Objects.isNull(appUser.getGender())) {
            switch(appUser.getGender()) {
                case MALE -> {
                    userDetails.setGender("Male");
                }

                case FEMALE ->  {
                    userDetails.setGender("Female");
                }

                case OTHER -> {
                    userDetails.setGender("Other");
                }
            }
        }

        // convert the birthdate and dateJoined
        if(!Objects.isNull(appUser.getBirthdate())) {
            userDetails.setBirthdate(appUser.getBirthdate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }

        if(!Objects.isNull(appUser.getJoinedDate())) {
            userDetails.setJoinedDate(appUser.getJoinedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }

        return userDetails;
    }
}
