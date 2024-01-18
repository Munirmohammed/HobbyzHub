package com.hobbyzhub.javabackend.securitymodule.advice;

public class MessagingException extends RuntimeException {
    public MessagingException() {
    }

    public MessagingException(String message) {
        super(message);
    }
}
