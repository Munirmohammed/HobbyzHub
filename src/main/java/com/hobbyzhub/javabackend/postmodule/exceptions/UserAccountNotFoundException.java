package com.hobbyzhub.javabackend.postmodule.exceptions;

public class UserAccountNotFoundException extends RuntimeException{
    public UserAccountNotFoundException(String message) {
        super(message);
    }

    public UserAccountNotFoundException(String message,Throwable cause) {
        super(message,cause);
    }
}
