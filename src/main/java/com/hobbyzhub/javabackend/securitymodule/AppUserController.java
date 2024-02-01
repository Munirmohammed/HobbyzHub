package com.hobbyzhub.javabackend.securitymodule;

import com.hobbyzhub.javabackend.securitymodule.advice.MessagingException;
import com.hobbyzhub.javabackend.securitymodule.advice.TokenValidationException;
import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.payload.request.OpUserAccountRequest;
import com.hobbyzhub.javabackend.securitymodule.payload.request.RefreshTokenRequest;
import com.hobbyzhub.javabackend.securitymodule.payload.request.SearchAccountsRequest;
import com.hobbyzhub.javabackend.securitymodule.payload.request.VerifyEmailRequest;
import com.hobbyzhub.javabackend.securitymodule.payload.response.RefreshTokenResponse;
import com.hobbyzhub.javabackend.securitymodule.payload.response.UserDetailsResponse;
import com.hobbyzhub.javabackend.securitymodule.util.def.EntityModelMapper;
import com.hobbyzhub.javabackend.securitymodule.util.def.JwtUtils;
import com.hobbyzhub.javabackend.securitymodule.util.def.controller.AppUserControllerDef;
import com.hobbyzhub.javabackend.securitymodule.util.def.service.AppUserServiceDef;
import com.hobbyzhub.javabackend.sharedexceptions.ServerErrorException;
import com.hobbyzhub.javabackend.sharedpayload.GenericResponse;
import com.hobbyzhub.javabackend.sharedutils.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
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

    @Autowired
    private JwtUtils jwtUtils;

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
            appUserService.markAccountNotNew(userId);

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

    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchAccounts(@RequestBody SearchAccountsRequest request) {
        List<AppUser> appUsers = appUserService.searchUsersByName(request.getSearchSlug(), request.getPage(), request.getSize());
        List<UserDetailsResponse> responseList = appUsers.parallelStream().map(user -> (UserDetailsResponse) super.mapEntityToPayload(user)).toList();

        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully retrieved user details",
            true,
            HttpStatus.OK.value(),
            responseList
        ));
    }

    @GetMapping("/data/{userId}")
    public ResponseEntity<?> getData(@PathVariable("userId") String userId){
        return new ResponseEntity<>(
                appUserService.findUserById(userId)
                ,HttpStatus.OK);
    }

    @PostMapping(value = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        AppUser user = null;
        try {
            String token;

            // Check if the token is provided in the header
            if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);

                // Validate the token and check if it is not expired
                boolean isValidToken = jwtUtils.validateToken(token);
                if (!isValidToken) {
                    throw new TokenValidationException("Token is expired or invalid");
                }

                // Get user details from the database
                user = appUserService.findUserById(jwtUtils.getUserIdFromToken(token));

                // Create Authentication object (You need to modify this part based on your UserDetailsImpl implementation)
                UserDetailsImpl userDetails = new UserDetailsImpl(user); // Modify this line according to your UserDetailsImpl

                // Build the response
                RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();
                refreshTokenResponse.setVerified(true);
                refreshTokenResponse.setToken(jwtUtils.generateToken(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())));
                refreshTokenResponse.setNewUser(user.isNewAccount());
                refreshTokenResponse.setCategoryStatus(user.isCategoryStatus());

                return ResponseEntity.ok().body(new GenericResponse<>(
                        apiVersion,
                        organizationName,
                        "Token verification successful",
                        true,
                        HttpStatus.OK.value(),
                        refreshTokenResponse
                ));
            } else {
                throw new TokenValidationException("Authorization header is missing or invalid");
            }
        } catch (TokenValidationException ex) {
            log.error("Token validation failed", ex);
            // Handle token validation error
            RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse();
            refreshTokenResponse.setVerified(false);
            refreshTokenResponse.setToken(null);
            assert user != null;
            refreshTokenResponse.setNewUser(user.isNewAccount());
            refreshTokenResponse.setCategoryStatus(user.isCategoryStatus());

            return ResponseEntity.ok().body(new GenericResponse<>(
                    apiVersion,
                    organizationName,
                    "Token verification failed",
                    false,
                    HttpStatus.OK.value(),
                    refreshTokenResponse
            ));
        } catch (Exception e) {
            log.error("Error refreshing token", e);
            throw new ServerErrorException("Error refreshing token");
        }
    }




}
