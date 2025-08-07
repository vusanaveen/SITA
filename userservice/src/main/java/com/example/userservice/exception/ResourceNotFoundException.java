package com.example.userservice.exception;

/**
 * Exception thrown when a requested resource is not found.
 * 
 * This exception is used when attempting to retrieve, update, or delete
 * a resource that does not exist in the system.
 * 
 * @author Senior Consultant
 * @version 1.0.0
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
