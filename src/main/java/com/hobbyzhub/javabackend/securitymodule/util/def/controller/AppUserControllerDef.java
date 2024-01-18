package com.hobbyzhub.javabackend.securitymodule.util.def.controller;

import com.hobbyzhub.javabackend.securitymodule.payload.request.GetAccountRequest;
import com.hobbyzhub.javabackend.securitymodule.payload.request.SendMailRequest;
import com.hobbyzhub.javabackend.securitymodule.payload.request.VerifyEmailRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AppUserControllerDef {
    ResponseEntity<?> createUserAccount(String email, String userId);
    ResponseEntity<?> sendOtp(SendMailRequest request, String intent);
    ResponseEntity<?> verifyOtp(VerifyEmailRequest request);
    ResponseEntity<?> getAccountById(GetAccountRequest request);
    ResponseEntity<?> deleteAccount(String id);
    ResponseEntity<?> updateAccountDetails(String userId, String fullName, String birthdate, String gender, MultipartFile profileImage, String bio);
    ResponseEntity<?> getAccountsList(String page, String size);
}
