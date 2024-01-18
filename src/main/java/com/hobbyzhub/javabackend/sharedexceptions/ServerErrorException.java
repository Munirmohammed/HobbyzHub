package com.hobbyzhub.javabackend.sharedexceptions;

public class ServerErrorException extends RuntimeException {
    public ServerErrorException() {
    }

    public ServerErrorException(String message) {
        super(message);
    }
}
