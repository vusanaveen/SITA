package com.example.orderservice.client;

import com.example.common.exception.InvalidUserException;
import com.example.orderservice.client.impl.UserServiceClientImpl;
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
 * @author Naveen Vusa
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceClientImpl Tests")
class UserServiceClientImplTest {

    @Mock
    private WebClient webClient;

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
        Mono<ResponseEntity<Void>> responseMono = Mono.just(ResponseEntity.ok().build());
        @SuppressWarnings("rawtypes")
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        @SuppressWarnings("rawtypes")
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Long.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(responseMono);

        boolean result = userServiceClient.userExists(1L);

        assertTrue(result);
        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("http://localhost:8081/users/{id}/exists", 1L);
    }

    @Test
    @DisplayName("Should return false when user does not exist")
    void shouldReturnFalseWhenUserDoesNotExist() {
        Mono<ResponseEntity<Void>> responseMono = Mono.error(new WebClientResponseException(
                HttpStatus.NOT_FOUND.value(), "Not Found", null, null, null));
        @SuppressWarnings("rawtypes")
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        @SuppressWarnings("rawtypes")
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Long.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(responseMono);

        boolean result = userServiceClient.userExists(999L);

        assertFalse(result);
        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("http://localhost:8081/users/{id}/exists", 999L);
    }

    @Test
    @DisplayName("Should throw InvalidUserException when WebClientResponseException occurs")
    void shouldThrowInvalidUserExceptionWhenWebClientResponseExceptionOccurs() {
        Mono<ResponseEntity<Void>> responseMono = Mono.error(new WebClientResponseException(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", null, null, null));
        @SuppressWarnings("rawtypes")
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        @SuppressWarnings("rawtypes")
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Long.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(responseMono);

        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userServiceClient.userExists(1L));
        assertTrue(exception.getMessage().contains("Error validating user"));
        
        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("http://localhost:8081/users/{id}/exists", 1L);
    }

    @Test
    @DisplayName("Should throw InvalidUserException when generic exception occurs")
    void shouldThrowInvalidUserExceptionWhenGenericExceptionOccurs() {
        Mono<ResponseEntity<Void>> responseMono = Mono.error(new RuntimeException("Network error"));
        @SuppressWarnings("rawtypes")
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        @SuppressWarnings("rawtypes")
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), any(Long.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(responseMono);

        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> userServiceClient.userExists(1L));
        assertTrue(exception.getMessage().contains("Error validating user"));
        
        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("http://localhost:8081/users/{id}/exists", 1L);
    }
}
