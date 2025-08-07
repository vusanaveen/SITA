package com.example.common.exception;

/**
 * Exception thrown when validation fails.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
