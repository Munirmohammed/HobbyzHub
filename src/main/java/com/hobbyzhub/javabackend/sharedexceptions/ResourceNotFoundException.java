package com.hobbyzhub.javabackend.sharedexceptions;

/**
 *
 * @author bikathi_martin
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
