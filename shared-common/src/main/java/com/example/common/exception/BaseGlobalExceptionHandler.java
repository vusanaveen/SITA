package com.example.common.exception;

import com.example.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


/**
 * Base global exception handler for all services.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@Slf4j
public abstract class BaseGlobalExceptionHandler {

    /**
     * Handle ResourceNotFoundException.
     * 
     * @param ex the exception
     * @return error response with 404 status
     */
    protected ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.notFound(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handle InvalidUserException.
     * 
     * @param ex the exception
     * @return error response with 400 status
     */
    protected ResponseEntity<ErrorResponse> handleInvalidUserException(InvalidUserException ex) {
        log.error("Invalid user: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.badRequest(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle ValidationException.
     * 
     * @param ex the exception
     * @return error response with 400 status
     */
    protected ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        log.error("Validation error: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.badRequest(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle InternalServerException.
     * 
     * @param ex the exception
     * @return error response with 500 status
     */
    protected ResponseEntity<ErrorResponse> handleInternalServerException(InternalServerException ex) {
        log.error("Internal server error: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.internalServerError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Handle HttpMessageNotReadableException for JSON parsing errors.
     *
     * @param ex the exception
     * @return error response with 400 status
     */
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("JSON parsing error: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.badRequest("Invalid input data provided");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle MethodArgumentTypeMismatchException for type conversion errors.
     * 
     * @param ex the exception
     * @return error response with 400 status
     */
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("Type mismatch error: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.badRequest("Invalid parameter type");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle MethodArgumentNotValidException for validation errors.
     * 
     * @param ex the exception
     * @return error response with 400 status
     */
    protected ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.error("Validation errors: {}", ex.getMessage());
        
        // Summarize validation failure without collecting field-by-field details per acceptance scope.
        ErrorResponse errorResponse = ErrorResponse.badRequest("Invalid input data provided");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle generic RuntimeException.
     * 
     * @param ex the exception
     * @return error response with 500 status
     */
    protected ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime error: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.internalServerError("An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Handle generic Exception.
     * 
     * @param ex the exception
     * @return error response with 500 status
     */
    protected ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Generic error: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = ErrorResponse.internalServerError("An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
