package com.hobbyzhub.javabackend.securitymodule.payload.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private String userId;
    private String email;
    private boolean newAccount;
}
