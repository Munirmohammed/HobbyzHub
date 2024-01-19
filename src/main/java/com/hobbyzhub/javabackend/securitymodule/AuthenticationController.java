package com.hobbyzhub.javabackend.securitymodule;

import com.hobbyzhub.javabackend.securitymodule.entity.AppUser;
import com.hobbyzhub.javabackend.securitymodule.payload.request.*;
import com.hobbyzhub.javabackend.securitymodule.payload.response.LoginResponse;
import com.hobbyzhub.javabackend.securitymodule.payload.response.RegisterResponse;
import com.hobbyzhub.javabackend.securitymodule.service.AppUserService;
import com.hobbyzhub.javabackend.securitymodule.util.def.EntityModelMapper;
import com.hobbyzhub.javabackend.securitymodule.util.def.JwtUtils;
import com.hobbyzhub.javabackend.securitymodule.util.def.UserDetailsImpl;
import com.hobbyzhub.javabackend.sharedexceptions.ServerErrorException;
import com.hobbyzhub.javabackend.sharedexceptions.SuccessWithErrorException;
import com.hobbyzhub.javabackend.sharedpayload.GenericResponse;
import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController extends EntityModelMapper {
    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping(value = "/register",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody OpUserAccountRequest request) {
        try {
            AppUser newAppUser = super.mapPayloadToEntity(request);
            AppUser credentials =  appUserService.createUserAccount(newAppUser);

            // generate response body
            RegisterResponse response = (RegisterResponse) super.mapEntityToPayload(credentials);

            return ResponseEntity.ok().body(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully registered new user",
                true,
                HttpStatus.OK.value(),
                response
            ));
        } catch (EntityExistsException ex) {
            throw new SuccessWithErrorException(ex.getMessage());
        }

    }
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginUser(@RequestBody OpUserAccountRequest request) {
        Authentication authentication
            = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if(authentication.isAuthenticated()) {
            return ResponseEntity.ok().body(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully logged in user",
                true,
                HttpStatus.OK.value(),
                LoginResponse.builder()
                    .token(jwtUtils.generateToken(authentication))
                    .email(userDetails.getEmail())
                    .newAccount(userDetails.isNewAccount())
                    .userId(userDetails.getUserId())
                .build()
            ));
        } else {
            log.error("Unknown error while logging in user in.");
            throw new ServerErrorException("Failed to log user in. Please try again.");
        }
    }

    @PutMapping(value = "/activate-account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> activateAccount(@RequestBody OpUserAccountRequest request) {
        appUserService.activateAccount(request.getEmail());
        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully activated user account",
            true,
            HttpStatus.OK.value(),
            null
        ));
    }

    @PutMapping(value = "/reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        AppUser AppUser = super.mapPayloadToEntity(request);
        AppUser.setPassword(request.getNewPassword());

        appUserService.resetPassword(AppUser);
        return ResponseEntity.ok().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Successfully reset user's password",
            true,
            HttpStatus.OK.value(),
            null
        ));
    }

    @DeleteMapping(value = "/delete-account", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAccount(@RequestBody OpUserAccountRequest request) {
        try {
            appUserService.deleteAccountByEmail(request.getEmail());

            return ResponseEntity.ok().body(new GenericResponse<>(
                apiVersion,
                organizationName,
                "Successfully deleted user account",
                true,
                HttpStatus.OK.value(),
                null
            ));
        } catch(EntityExistsException ex) {
            throw new SuccessWithErrorException(ex.getMessage());
        }
    }
}
