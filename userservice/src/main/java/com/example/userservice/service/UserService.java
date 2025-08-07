package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;

import java.util.List;

/**
 * Service interface for User business logic.
 * 
 * This interface defines the business operations for user management
 * including CRUD operations and validation.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
public interface UserService {

    /**
     * Create a new user.
     * 
     * @param userRequest the user data to create
     * @return the created user response
     */
    UserResponse createUser(UserRequest userRequest);

    /**
     * Get a user by ID.
     * 
     * @param id the user ID
     * @return the user response
     * @throws ResourceNotFoundException if user not found
     */
    UserResponse getUserById(Long id);

    /**
     * Get all users.
     * 
     * @return list of all users
     */
    List<UserResponse> getAllUsers();

    /**
     * Update a user by ID.
     * 
     * @param id the user ID
     * @param userRequest the updated user data
     * @return the updated user response
     * @throws ResourceNotFoundException if user not found
     */
    UserResponse updateUser(Long id, UserRequest userRequest);

    /**
     * Delete a user by ID.
     * 
     * @param id the user ID
     * @throws ResourceNotFoundException if user not found
     */
    void deleteUser(Long id);

    /**
     * Check if a user exists by ID.
     * 
     * @param id the user ID
     * @return true if user exists, false otherwise
     */
    boolean userExists(Long id);
}
