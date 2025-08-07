package com.example.common.exception;

/**
 * Exception thrown for internal server errors.
 * 
 * @author Naveen Vusa
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
