package com.hobbyzhub.javabackend.postmodule.exceptions;

public class CommentNotFoundException extends Throwable {
    public CommentNotFoundException(String message) {
        super(message);
    }
    public CommentNotFoundException(String message, Throwable throwable){
        super(message,throwable);
    }

}
