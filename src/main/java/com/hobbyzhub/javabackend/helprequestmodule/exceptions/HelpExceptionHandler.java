package com.hobbyzhub.javabackend.helprequestmodule.exceptions;
import com.hobbyzhub.javabackend.helprequestmodule.responses.HelpGenericResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HelpExceptionHandler {

    @Value("${application.api.version}")
    private String apiVersion;

    @Value("${application.organization.name}")
    private String organizationName;

    @ExceptionHandler(value = {HelpNotFoundException.class})
    public ResponseEntity<?> handleHelpNotFoundException(HelpNotFoundException helpNotFoundException){
        HelpException exception = HelpException.builder()
                .message(helpNotFoundException.getMessage())
                .cause(helpNotFoundException.getCause())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(new HelpGenericResponseException<>(
                apiVersion,
                organizationName,
                "help not found.",
                false,
                HttpStatus.NOT_FOUND.value(),
                exception
        ),HttpStatus.NOT_FOUND);
    }
}
