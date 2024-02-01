package com.hobbyzhub.javabackend.securitymodule;

import com.hobbyzhub.chatservice.advice.EntityNotFoundException;
import com.hobbyzhub.javabackend.followersmodule.payload.response.UserPreviewResponse;
import com.hobbyzhub.javabackend.postmodule.service.PostService;
import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.service.AppUserService;
import com.hobbyzhub.javabackend.securitymodule.util.def.EntityModelMapper;
import com.hobbyzhub.javabackend.sharedexceptions.ServerErrorException;
import com.hobbyzhub.javabackend.sharedpayload.SharedAccountsInformation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class SharedAccounts {
    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PostService postService;

    public SharedAccountsInformation retrieveSharedAccount(String userId) {
        AppUser appUser = appUserService.findUserById(userId);
        SharedAccountsInformation userDetails = SharedAccountsInformation.builder()
            .email(appUser.getEmail())
            .userId(appUser.getUserId())
            .fullName(appUser.getFullName())
            .bio(appUser.getBio())
            .profileImage(appUser.getProfileImage())
                .categoryStatus(appUser.isCategoryStatus())
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

    public UserPreviewResponse getData(String userId) {
        AppUser user = appUserService.findUserById(userId);
        return mapUserRelationshipToUserPreview(user);
    }

    private UserPreviewResponse mapUserRelationshipToUserPreview(AppUser user) {
        try {
            if (user != null) {
                return UserPreviewResponse.builder()
                        .userId(user.getUserId())
                        .fullName(user.getFullName())  // Set fullName property
                        .profileImage(user.getProfileImage())  // Set profileImage property
                        .build();
            } else {
                // Handle the case where the user is not found
                throw new EntityNotFoundException("User not found");
            }
        } catch (EntityNotFoundException ex) {
            // Log the error and re-throw the exception
            try {
                throw ex;
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            // Log other exceptions and throw a ServerErrorException
            throw new ServerErrorException("Error mapping user relationship to user preview");
        }
    }


    public ResponseEntity<Integer> getPostCount(String userId) {
        int postCount = postService.getPostCount(userId);
        return ResponseEntity.ok(postCount);
    }

    public void updateUserInformation(String userId, SharedAccountsInformation updatedInformation) {
        try {
            AppUser appUser = appUserService.findUserById(userId);

            if (appUser != null) {
                // Update user information
                appUser.setCategoryStatus(updatedInformation.isCategoryStatus());

                // Save the updated user to the database
                appUserService.saveUser(appUser);
            } else {
                // Handle the case where the user is not found
                throw new EntityNotFoundException("User not found with ID: " + userId);
            }
        } catch (EntityNotFoundException ex) {
            // Handle the case where the user is not found
            try {
                throw ex; // Re-throw the exception to be handled at a higher level if needed
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            // Handle other exceptions
            throw new ServerErrorException("Error updating user information");
        }
    }

}
