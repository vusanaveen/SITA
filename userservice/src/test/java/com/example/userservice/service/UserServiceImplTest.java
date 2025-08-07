package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.exception.ValidationException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for UserServiceImpl.
 * 
 * Tests all service methods with mocked dependencies and 100% code coverage.
 * 
 * @author Senior Consultant
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserRequest testUserRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");

        testUserRequest = new UserRequest();
        testUserRequest.setUsername("testuser");
        testUserRequest.setPassword("password123");
        testUserRequest.setEmail("test@example.com");
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserResponse result = userService.createUser(testUserRequest);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getEmail(), result.getEmail());
        
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when username already exists")
    void shouldThrowValidationExceptionWhenUsernameExists() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.createUser(testUserRequest));
        assertEquals("Username already exists: testuser", exception.getMessage());
        
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when email already exists")
    void shouldThrowValidationExceptionWhenEmailExists() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.createUser(testUserRequest));
        assertEquals("Email already exists: test@example.com", exception.getMessage());
        
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when user request is null")
    void shouldThrowValidationExceptionWhenUserRequestIsNull() {
        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.createUser(null));
        assertEquals("User request cannot be null", exception.getMessage());
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void shouldGetUserByIdSuccessfully() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        UserResponse result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getEmail(), result.getEmail());
        
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found by ID")
    void shouldThrowResourceNotFoundExceptionWhenUserNotFoundById() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(1L));
        assertEquals("User not found with ID: 1", exception.getMessage());
        
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should get all users successfully")
    void shouldGetAllUsersSuccessfully() {
        // Given
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testuser2");
        user2.setPassword("password456");
        user2.setEmail("test2@example.com");
        
        List<User> users = Arrays.asList(testUser, user2);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserResponse> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testUser.getId(), result.get(0).getId());
        assertEquals(user2.getId(), result.get(1).getId());
        
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        // Given
        UserRequest updateRequest = new UserRequest();
        updateRequest.setUsername("updateduser");
        updateRequest.setPassword("newpassword");
        updateRequest.setEmail("updated@example.com");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUsername("updateduser")).thenReturn(false);
        when(userRepository.existsByEmail("updated@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserResponse result = userService.updateUser(1L, updateRequest);

        // Then
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        
        verify(userRepository).findById(1L);
        verify(userRepository).existsByUsername("updateduser");
        verify(userRepository).existsByEmail("updated@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent user")
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(1L, testUserRequest));
        assertEquals("User not found with ID: 1", exception.getMessage());
        
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when updating with existing username")
    void shouldThrowValidationExceptionWhenUpdatingWithExistingUsername() {
        // Given
        UserRequest updateRequest = new UserRequest();
        updateRequest.setUsername("existinguser");
        updateRequest.setPassword("password123");
        updateRequest.setEmail("test@example.com");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.updateUser(1L, updateRequest));
        assertEquals("Username already exists: existinguser", exception.getMessage());
        
        verify(userRepository).findById(1L);
        verify(userRepository).existsByUsername("existinguser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when updating with existing email")
    void shouldThrowValidationExceptionWhenUpdatingWithExistingEmail() {
        // Given
        UserRequest updateRequest = new UserRequest();
        updateRequest.setUsername("testuser");
        updateRequest.setPassword("password123");
        updateRequest.setEmail("existing@example.com");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.updateUser(1L, updateRequest));
        assertEquals("Email already exists: existing@example.com", exception.getMessage());
        
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("existing@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent user")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentUser() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(false);

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(1L));
        assertEquals("User not found with ID: 1", exception.getMessage());
        
        verify(userRepository).existsById(1L);
        verify(userRepository, never()).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("Should check if user exists successfully")
    void shouldCheckIfUserExistsSuccessfully() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(2L)).thenReturn(false);

        // When & Then
        assertTrue(userService.userExists(1L));
        assertFalse(userService.userExists(2L));
        
        verify(userRepository).existsById(1L);
        verify(userRepository).existsById(2L);
    }
}
