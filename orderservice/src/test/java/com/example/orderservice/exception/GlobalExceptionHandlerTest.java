package com.example.orderservice.exception;

import com.example.common.dto.ErrorResponse;
import com.example.common.exception.InvalidUserException;
import com.example.common.exception.ResourceNotFoundException;
import com.example.common.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldMapResourceNotFoundTo404() {
        ResponseEntity<ErrorResponse> resp = handler.handleResourceNotFoundException(new ResourceNotFoundException("not found"));
        assertEquals(404, resp.getStatusCode().value());
        assertEquals("not found", resp.getBody().getError());
    }

    @Test
    void shouldMapInvalidUserTo400() {
        ResponseEntity<ErrorResponse> resp = handler.handleInvalidUserException(new InvalidUserException("bad user"));
        assertEquals(400, resp.getStatusCode().value());
        assertEquals("bad user", resp.getBody().getError());
    }

    @Test
    void shouldMapValidationTo400() {
        ResponseEntity<ErrorResponse> resp = handler.handleValidationException(new ValidationException("invalid"));
        assertEquals(400, resp.getStatusCode().value());
        assertEquals("invalid", resp.getBody().getError());
    }

    @Test
    void shouldMapGenericRuntimeTo500() {
        ResponseEntity<ErrorResponse> resp = handler.handleRuntimeException(new RuntimeException("boom"));
        assertEquals(500, resp.getStatusCode().value());
        assertTrue(resp.getBody().getError().contains("unexpected"));
    }
}


