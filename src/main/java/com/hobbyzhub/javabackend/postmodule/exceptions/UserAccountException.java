package com.hobbyzhub.javabackend.postmodule.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class UserAccountException {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus status;
}
