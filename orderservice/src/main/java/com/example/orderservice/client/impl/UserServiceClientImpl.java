package com.example.orderservice.client.impl;

import com.example.orderservice.client.UserServiceClient;
import com.example.common.exception.InvalidUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;

/**
 * Implementation of UserServiceClient interface.
 * 
 * This class provides HTTP communication with UserService
 * for user validation operations.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserServiceClientImpl implements UserServiceClient {

    private final WebClient webClient;

    @Value("${user-service.base-url}")
    private String userServiceBaseUrl;

    @Value("${user-service.timeout:5000}")
    private int timeout;

    @Override
    public boolean userExists(Long userId) {
        log.debug("Checking if user exists with ID: {}", userId);
        
        try {
            webClient.get()
                    .uri(userServiceBaseUrl + "/users/{id}/exists", userId)
                    .retrieve()
                    .toBodilessEntity()
                    .timeout(Duration.ofMillis(timeout))
                    .block();
            
            log.debug("User with ID {} exists", userId);
            return true;
            
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.debug("User with ID {} does not exist", userId);
                return false;
            }
            log.error("Error checking user existence for ID {}: {}", userId, e.getMessage());
            throw new InvalidUserException("Error validating user: " + e.getMessage(), e);
            
        } catch (Exception e) {
            log.error("Unexpected error checking user existence for ID {}: {}", userId, e.getMessage());
            throw new InvalidUserException("Error validating user: " + e.getMessage(), e);
        }
    }
}
