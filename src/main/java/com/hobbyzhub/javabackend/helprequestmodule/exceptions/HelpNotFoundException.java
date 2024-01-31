package com.hobbyzhub.javabackend.helprequestmodule.exceptions;

public class HelpNotFoundException extends RuntimeException{
    public HelpNotFoundException(String message) {
        super(message);
    }

    public HelpNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
