package com.example.orderservice.exception;

/**
 * Exception thrown when a user is not found or invalid.
 * 
 * This exception is used when attempting to create an order for
 * a user that does not exist in the UserService.
 * 
 * @author Senior Consultant
 * @version 1.0.0
 */
public class InvalidUserException extends RuntimeException {

    public InvalidUserException(String message) {
        super(message);
    }

    public InvalidUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
