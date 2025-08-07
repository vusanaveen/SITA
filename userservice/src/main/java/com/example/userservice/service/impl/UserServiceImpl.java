package com.example.userservice.service.impl;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.common.exception.ResourceNotFoundException;
import com.example.common.exception.ValidationException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of UserService interface.
 * 
 * This class provides the business logic for user management operations
 * including CRUD operations, validation, and data transformation.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        log.info("Creating new user");
        
        validateUserRequest(userRequest);
        
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setEmail(userRequest.getEmail());
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        
        return mapToUserResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.info("Retrieving user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        
        log.info("User retrieved successfully: {}", user.getUsername());
        return mapToUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        log.info("Retrieving all users");
        
        List<User> users = userRepository.findAll();
        log.info("Retrieved {} users", users.size());
        
        return users.stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        log.info("Updating user with ID: {}", id);
        
        if (userRequest == null) {
            throw new ValidationException("User request cannot be null");
        }
        
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        
        // Requirement: Duplicate handling is not enforced. No repository duplicate checks here.
        
        existingUser.setUsername(userRequest.getUsername());
        existingUser.setPassword(userRequest.getPassword());
        existingUser.setEmail(userRequest.getEmail());
        
        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully: {}", updatedUser.getUsername());
        
        return mapToUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(Long id) {
        log.debug("Checking if user exists with ID: {}", id);
        return userRepository.existsById(id);
    }

    /**
     * Validate user request data for creation.
     * 
     * @param userRequest the user request to validate
     * @throws ValidationException if validation fails
     */
    private void validateUserRequest(UserRequest userRequest) {
        if (userRequest == null) {
            throw new ValidationException("User request cannot be null");
        }
        // Requirement: Duplicate handling is not enforced. No repository duplicate checks here.
    }

    /**
     * Map User entity to UserResponse DTO.
     * 
     * @param user the user entity
     * @return the user response DTO
     */
    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
