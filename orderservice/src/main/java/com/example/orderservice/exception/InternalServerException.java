package com.example.orderservice.exception;

/**
 * Exception thrown when an internal server error occurs.
 * 
 * This exception is used for unexpected errors that occur
 * during application execution.
 * 
 * @author Senior Consultant
 * @version 1.0.0
 */
public class InternalServerException extends RuntimeException {

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
