package com.hobbyzhub.javabackend.securitymodule.util.def.service;

import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AppUserServiceDef {
    void registerNewAccount(AppUser appUser);
    AppUser findUserById(String userId);
    AppUser updateUserDetails(String userId, String fullName, String birthdate, String gender, String bio, MultipartFile profileImage);
    void verifyOTP(String email, Integer otp);
    void deleteAccountByEmail(String email);
    void sendVerificationEmail(String email, String intent);
    List<AppUser> getAccountsList(int pageNumber, int pageSize);
    AppUser createUserAccount(AppUser userCredential);
    void markAccountNotNew(String userId);
    void activateAccount(String email);
    void resetPassword(AppUser userCredential);
    List<AppUser> searchUsersByName(String searchSlug, Integer page, Integer size);
}
