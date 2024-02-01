package com.hobbyzhub.javabackend.securitymodule.util.def.controller;

import com.hobbyzhub.javabackend.securitymodule.payload.request.OpUserAccountRequest;
import com.hobbyzhub.javabackend.securitymodule.payload.request.VerifyEmailRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AppUserControllerDef {
    ResponseEntity<?> sendOtp(OpUserAccountRequest request, String intent);
    ResponseEntity<?> verifyOtp(VerifyEmailRequest request);
    ResponseEntity<?> getAccountById(OpUserAccountRequest request);
    ResponseEntity<?> deleteAccount(String id);
    ResponseEntity<?> updateAccountDetails(String userId, String fullName, String birthdate, String gender, MultipartFile profileImage, String bio);
    ResponseEntity<?> getAccountsList(String page, String size);
}
