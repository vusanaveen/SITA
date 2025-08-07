package com.example.orderservice.client;

import com.example.orderservice.client.impl.UserServiceClientImpl;
import com.example.orderservice.exception.InvalidUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class for UserServiceClientImpl.
 * 
 * Tests all client methods with mocked WebClient and 100% code coverage.
 * 
 * @author Senior Consultant
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceClientImpl Tests")
class UserServiceClientImplTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private UserServiceClientImpl userServiceClient;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userServiceClient, "userServiceBaseUrl", "http://localhost:8081");
        ReflectionTestUtils.setField(userServiceClient, "timeout", 5000);
    }

    @Test
    @DisplayName("Should return true when user exists")
    void shouldReturnTrueWhenUserExists() {
        // Given
        Mono<ResponseEntity<Void>> responseMono = Mono.just(ResponseEntity.ok().build());
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Long.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(responseMono);

        // When
        boolean result = userServiceClient.userExists(1L);

        // Then
        assertTrue(result);
        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("http://localhost:8081/users/{id}/exists", 1L);
    }

    @Test
    @DisplayName("Should return false when user does not exist")
    void shouldReturnFalseWhenUserDoesNotExist() {
        // Given
        Mono<ResponseEntity<Void>> responseMono = Mono.error(new WebClientResponseException(
                HttpStatus.NOT_FOUND.value(), "Not Found", null, null, null));
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Long.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(responseMono);

        // When
        boolean result = userServiceClient.userExists(999L);

        // Then
        assertFalse(result);
        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("http://localhost:8081/users/{id}/exists", 999L);
    }

    @Test
    @DisplayName("Should throw InvalidUserException when WebClientResponseException occurs")
    void shouldThrowInvalidUserExceptionWhenWebClientResponseExceptionOccurs() {
        // Given
        Mono<ResponseEntity<Void>> responseMono = Mono.error(new WebClientResponseException(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", null, null, null));
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Long.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(responseMono);

        // When & Then
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userServiceClient.userExists(1L));
        assertTrue(exception.getMessage().contains("Error validating user"));
        
        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("http://localhost:8081/users/{id}/exists", 1L);
    }

    @Test
    @DisplayName("Should throw InvalidUserException when generic exception occurs")
    void shouldThrowInvalidUserExceptionWhenGenericExceptionOccurs() {
        // Given
        Mono<ResponseEntity<Void>> responseMono = Mono.error(new RuntimeException("Network error"));
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Long.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(responseMono);

        // When & Then
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userServiceClient.userExists(1L));
        assertTrue(exception.getMessage().contains("Error validating user"));
        
        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("http://localhost:8081/users/{id}/exists", 1L);
    }
}
