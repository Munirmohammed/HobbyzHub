package com.hobbyzhub.javabackend.sharedexceptions;

public class SuccessWithErrorException extends RuntimeException {
    public SuccessWithErrorException() {
    }

    public SuccessWithErrorException(String message) {
        super(message);
    }
}
