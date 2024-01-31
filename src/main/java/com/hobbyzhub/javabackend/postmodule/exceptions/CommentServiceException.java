package com.hobbyzhub.javabackend.postmodule.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentServiceException {
    private String message;
    private HttpStatus status;
    private Throwable throwable;
}
