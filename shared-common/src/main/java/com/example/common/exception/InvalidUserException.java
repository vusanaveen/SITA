package com.example.common.exception;

/**
 * Exception thrown when user validation fails.
 * 
 * @author Naveen Vusa
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
