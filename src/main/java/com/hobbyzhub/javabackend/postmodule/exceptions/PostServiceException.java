package com.hobbyzhub.javabackend.postmodule.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;


@Builder
@Data
public class PostServiceException {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;
}
