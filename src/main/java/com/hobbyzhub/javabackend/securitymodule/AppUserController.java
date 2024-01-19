package com.hobbyzhub.javabackend.securitymodule;

import com.hobbyzhub.javabackend.securitymodule.advice.MessagingException;
import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.payload.request.OpUserAccountRequest;
import com.hobbyzhub.javabackend.securitymodule.payload.request.VerifyEmailRequest;
import com.hobbyzhub.javabackend.securitymodule.payload.response.UserDetailsResponse;
import com.hobbyzhub.javabackend.securitymodule.util.def.EntityModelMapper;
import com.hobbyzhub.javabackend.securitymodule.util.def.controller.AppUserControllerDef;
import com.hobbyzhub.javabackend.securitymodule.util.def.service.AppUserServiceDef;
import com.hobbyzhub.javabackend.sharedexceptions.ServerErrorException;
import com.hobbyzhub.javabackend.sharedpayload.GenericResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/accounts")
public class AppUserController extends EntityModelMapper implements AppUserControllerDef {
    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @Autowired
    private AppUserServiceDef appUserService;

    @Override
    @PostMapping(value = "/email-otp")
    public ResponseEntity<?> sendOtp(@RequestBody OpUserAccountRequest request, @RequestParam String intent) {
        try {
            appUserService.sendVerificationEmail(request.getEmail(), intent);

            return ResponseEntity.ok(new GenericResponse<>(
                    apiVersion,
                    organizationName,
                    "Successfully sent email",
                    true,
                    HttpStatus.OK.value(),
                    null
            ));
        } catch(MessagingException exception) {
            log.error("Failure when trying to send email");
            throw new ServerErrorException(exception.getMessage());
        } catch(EntityNotFoundException exception) {
            log.error("Attempt to send email to unknown destination");
            throw new ServerErrorException(exception.getMessage());
        }
    }

    @Override
    @PutMapping(value = "/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyEmailRequest request) {
        appUserService.verifyOTP(request.getEmail(), request.getTemporaryOtp());
        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "OTP verification successful",
            true,
            HttpStatus.OK.value(),
            null
        ));
    }

    @Override
    @PostMapping(value = "/get-details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccountById(@RequestBody OpUserAccountRequest request) {
        try {
            AppUser user = appUserService.findUserById(request.getUserId());
            UserDetailsResponse responsePayload = (UserDetailsResponse) super.mapEntityToPayload(user);

            return ResponseEntity.ok().body(new GenericResponse<>(
                    apiVersion,
                    organizationName,
                    "Found user account",
                    true,
                    HttpStatus.OK.value(),
                    responsePayload
            ));
        } catch(EntityNotFoundException ex) {
            log.error("Failed attempt at getting user of non-exiting userId");
            throw new ServerErrorException(ex.getMessage());
        }

    }

    @Override
    @DeleteMapping(value = "/delete-account/{email}")
    public ResponseEntity<?> deleteAccount(@RequestParam String email) {
        appUserService.deleteAccountByEmail(email);

        return ResponseEntity.ok().body(null);
    }

    @Override
    @PutMapping(value = "/update-details")
    public ResponseEntity<?> updateAccountDetails(
            @RequestParam("userId") String userId,
            @RequestParam("fullName") String fullName,
            @RequestParam("birthdate") String birthdate,
            @RequestParam("gender") String gender,
            @RequestParam(required = false, name = "profileImage") MultipartFile profileImage,
            @RequestParam(name = "bio")String bio) {
        AppUser appUser = null;
        try {
            if(Objects.isNull(profileImage)) {
                appUser = appUserService.updateUserDetails(userId, fullName, birthdate, gender, bio, null);
            } else {
                appUser = appUserService.updateUserDetails(userId, fullName, birthdate, gender, bio, profileImage);
            }

            UserDetailsResponse response = (UserDetailsResponse) super.mapEntityToPayload(appUser);
            // authServiceFeign.markAccountAsNotNew(userId);

            return ResponseEntity.ok().body(new GenericResponse<>(
                    apiVersion,
                    organizationName,
                    "Successfully updated user details",
                    true,
                    HttpStatus.OK.value(),
                    response
            ));
        } catch(EntityNotFoundException ex) {
            log.error("Failed to update details for non-existent userId");
            throw new ServerErrorException(ex.getMessage());
        }
    }

    @Override
    @GetMapping(value = "/get-list")
    public ResponseEntity<?> getAccountsList(
            @RequestParam(defaultValue = "0") String page, @RequestParam(required = false, defaultValue = "100") String size) {
        int pageNumber = Integer.parseInt(page);
        int pageSize = Integer.parseInt(size);

        List<AppUser> accountsList = appUserService.getAccountsList(pageNumber, pageSize);
        List<UserDetailsResponse> accountsResponseList =
                accountsList.parallelStream().map(super::mapEntityToPayload).toList().parallelStream().map(UserDetailsResponse::new).toList();

        return ResponseEntity.ok().body(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully updated user details",
                true,
                HttpStatus.OK.value(),
                accountsResponseList
        ));
    }
    @GetMapping("/data/{userId}")
    public ResponseEntity<?> getData(@PathVariable("userId") String userId){
        return new ResponseEntity<>(
                appUserService.findUserById(userId)
                ,HttpStatus.OK);
    }
}
