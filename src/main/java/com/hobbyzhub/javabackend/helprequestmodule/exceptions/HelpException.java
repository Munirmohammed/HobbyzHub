package com.hobbyzhub.javabackend.helprequestmodule.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HelpException {
    private String message;
    private HttpStatus status;
    private Throwable cause;
}
