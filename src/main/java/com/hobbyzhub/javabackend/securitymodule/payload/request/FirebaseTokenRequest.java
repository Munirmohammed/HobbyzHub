package com.hobbyzhub.javabackend.securitymodule.payload.request;

import lombok.Data;

@Data
public class FirebaseTokenRequest {
    private String userId;
    private String firebaseToken;
}

