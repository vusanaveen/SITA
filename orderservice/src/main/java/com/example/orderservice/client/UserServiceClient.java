package com.example.orderservice.client;

/**
 * Client interface for UserService communication.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
public interface UserServiceClient {

    /**
     * Check if a user exists by ID.
     * 
     * @param userId the user ID to check
     * @return true if user exists, false otherwise
     */
    boolean userExists(Long userId);
}
