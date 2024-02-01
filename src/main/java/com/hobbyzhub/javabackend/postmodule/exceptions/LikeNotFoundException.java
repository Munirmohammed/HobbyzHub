package com.hobbyzhub.javabackend.postmodule.exceptions;

public class LikeNotFoundException extends RuntimeException{
    public LikeNotFoundException(String message) {
        super(message);
    }
    public LikeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
