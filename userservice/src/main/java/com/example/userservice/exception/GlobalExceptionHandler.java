package com.example.userservice.exception;

import com.example.common.dto.ErrorResponse;
import com.example.common.exception.BaseGlobalExceptionHandler;
import com.example.common.exception.InternalServerException;
import com.example.common.exception.ResourceNotFoundException;
import com.example.common.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Global exception handler for UserService.
 * 
 * This class provides centralized exception handling for all REST endpoints
 * with proper HTTP status codes and error responses.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends BaseGlobalExceptionHandler {

        /**
     * Handle ResourceNotFoundException.
     * 
     * @param ex the exception
     * @return error response with 404 status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return super.handleResourceNotFoundException(ex);
    }

    /**
     * Handle ValidationException.
     * 
     * @param ex the exception
     * @return error response with 400 status
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        return super.handleValidationException(ex);
    }

    /**
     * Handle InternalServerException.
     * 
     * @param ex the exception
     * @return error response with 500 status
     */
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(InternalServerException ex) {
        return super.handleInternalServerException(ex);
    }

    /**
     * Handle HttpMessageNotReadableException for JSON parsing errors.
     *
     * @param ex the exception
     * @return error response with 400 status
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return super.handleHttpMessageNotReadableException(ex);
    }

    /**
     * Handle MethodArgumentTypeMismatchException for type conversion errors.
     * 
     * @param ex the exception
     * @return error response with 400 status
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return super.handleMethodArgumentTypeMismatchException(ex);
    }

    /**
     * Handle MethodArgumentNotValidException for validation errors.
     * 
     * @param ex the exception
     * @return error response with 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        return super.handleValidationErrors(ex);
    }

    /**
     * Handle generic RuntimeException.
     * 
     * @param ex the exception
     * @return error response with 500 status
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        return super.handleRuntimeException(ex);
    }

    /**
     * Handle generic Exception.
     * 
     * @param ex the exception
     * @return error response with 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return super.handleGenericException(ex);
    }


}
