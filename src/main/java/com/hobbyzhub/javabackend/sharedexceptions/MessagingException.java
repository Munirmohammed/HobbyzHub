package com.hobbyzhub.javabackend.sharedexceptions;

public class MessagingException extends RuntimeException {
    public MessagingException() {
    }

    public MessagingException(String message) {
        super(message);
    }
}
