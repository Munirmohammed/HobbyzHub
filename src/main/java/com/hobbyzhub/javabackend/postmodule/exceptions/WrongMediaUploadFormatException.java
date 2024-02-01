package com.hobbyzhub.javabackend.postmodule.exceptions;

public class WrongMediaUploadFormatException extends RuntimeException{
    public WrongMediaUploadFormatException(String message) {
        super(message);
    }

    public WrongMediaUploadFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
