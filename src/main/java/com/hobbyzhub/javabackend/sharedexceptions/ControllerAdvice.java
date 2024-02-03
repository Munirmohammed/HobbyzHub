package com.hobbyzhub.javabackend.sharedexceptions;

import com.hobbyzhub.javabackend.sharedpayload.GenericResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerAdvice {
    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @ExceptionHandler(value = ServerErrorException.class)
    public ResponseEntity<?> handleServerErrorException(ServerErrorException ex, WebRequest req) {
        return ResponseEntity.internalServerError().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            ex.getMessage(),
            false,
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            null
        ));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex, WebRequest re) {
        return ResponseEntity.internalServerError().body(new GenericResponse<>(
            apiVersion,
            organizationName,
            "Request failed with unknown reason",
            false,
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            null
        ));
    }
}
