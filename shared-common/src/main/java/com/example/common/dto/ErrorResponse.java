package com.example.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for error responses.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard Error Response Structure")
public class ErrorResponse {

    @Schema(description = "Error message",
            examples = {
                "User not found with ID: 123",
                "Order not found with ID: 456",
                "Invalid input data provided",
                "Invalid input data provided",
                "Database connection failed",
                "Internal server error occurred"
            })
    private String error;

    @Schema(description = "HTTP status code",
            examples = {"400", "404", "500"})
    private Integer status;

    @Schema(description = "Timestamp of the error",
            example = "2025-08-07T21:00:00Z")
    private String timestamp;

    /**
     * Creates a 400 Bad Request error response.
     */
    public static ErrorResponse badRequest(String message) {
        return new ErrorResponse(message, 400, LocalDateTime.now().toString());
    }

    /**
     * Creates a 404 Not Found error response.
     */
    public static ErrorResponse notFound(String message) {
        return new ErrorResponse(message, 404, LocalDateTime.now().toString());
    }

    /**
     * Creates a 500 Internal Server Error response.
     */
    public static ErrorResponse internalServerError(String message) {
        return new ErrorResponse(message, 500, LocalDateTime.now().toString());
    }

    /**
     * Creates an error response with custom status code.
     */
    public static ErrorResponse of(String message, Integer status) {
        return new ErrorResponse(message, status, LocalDateTime.now().toString());
    }
}
