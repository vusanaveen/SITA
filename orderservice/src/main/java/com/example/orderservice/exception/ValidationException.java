package com.example.orderservice.exception;

/**
 * Exception thrown when validation fails.
 * 
 * This exception is used when input data fails validation
 * rules or business logic validation.
 * 
 * @author Naveen V
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
