package com.hobbyzhub.javabackend.securitymodule.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenResponse {
    private boolean verified;
    private String token;
    private boolean newUser;
    private boolean categoryStatus;
}
