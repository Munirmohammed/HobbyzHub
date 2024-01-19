package com.hobbyzhub.javabackend.securitymodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpUserAccountRequest {
    private String userId;
    private String email;
    private String password;
}
