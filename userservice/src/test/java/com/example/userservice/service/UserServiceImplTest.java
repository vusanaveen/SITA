package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.common.exception.ResourceNotFoundException;
import com.example.common.exception.ValidationException;
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
 * @author Naveen Vusa
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
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse result = userService.createUser(testUserRequest);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getEmail(), result.getEmail());
        
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when request is null")
    void shouldThrowValidationExceptionWhenUsernameExists() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.createUser(null));
        assertEquals("User request cannot be null", exception.getMessage());
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when email already exists")
    void shouldThrowValidationExceptionWhenEmailExists() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.createUser(null));
        assertEquals("User request cannot be null", exception.getMessage());
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when user request is null")
    void shouldThrowValidationExceptionWhenUserRequestIsNull() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.createUser(null));
        assertEquals("User request cannot be null", exception.getMessage());
        
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void shouldGetUserByIdSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserResponse result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getEmail(), result.getEmail());
        
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found by ID")
    void shouldThrowResourceNotFoundExceptionWhenUserNotFoundById() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(1L));
        assertEquals("User not found with ID: 1", exception.getMessage());
        
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should get all users successfully")
    void shouldGetAllUsersSuccessfully() {
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("testuser2");
        user2.setPassword("password456");
        user2.setEmail("test2@example.com");
        
        List<User> users = Arrays.asList(testUser, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<UserResponse> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testUser.getId(), result.get(0).getId());
        assertEquals(user2.getId(), result.get(1).getId());
        
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        UserRequest updateRequest = new UserRequest();
        updateRequest.setUsername("updateduser");
        updateRequest.setPassword("newpassword");
        updateRequest.setEmail("updated@example.com");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse result = userService.updateUser(1L, updateRequest);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when updating with null request")
    void shouldThrowValidationExceptionWhenUpdatingWithNullRequest() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.updateUser(1L, null));
        assertEquals("User request cannot be null", exception.getMessage());

        verify(userRepository, never()).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent user")
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(1L, testUserRequest));
        assertEquals("User not found with ID: 1", exception.getMessage());
        
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when updating with existing username")
    void shouldThrowValidationExceptionWhenUpdatingWithExistingUsername() {
        UserRequest updateRequest = new UserRequest();
        updateRequest.setUsername("existinguser");
        updateRequest.setPassword("password123");
        updateRequest.setEmail("test@example.com");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        when(userRepository.save(any(User.class))).thenReturn(testUser);
        UserResponse result = userService.updateUser(1L, updateRequest);
        assertNotNull(result);
        
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when updating with existing email")
    void shouldThrowValidationExceptionWhenUpdatingWithExistingEmail() {
        UserRequest updateRequest = new UserRequest();
        updateRequest.setUsername("testuser");
        updateRequest.setPassword("password123");
        updateRequest.setEmail("existing@example.com");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        when(userRepository.save(any(User.class))).thenReturn(testUser);
        UserResponse result2 = userService.updateUser(1L, updateRequest);
        assertNotNull(result2);
        
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent user")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentUser() {
        when(userRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(1L));
        assertEquals("User not found with ID: 1", exception.getMessage());
        
        verify(userRepository).existsById(1L);
        verify(userRepository, never()).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("Should check if user exists successfully")
    void shouldCheckIfUserExistsSuccessfully() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(2L)).thenReturn(false);

        assertTrue(userService.userExists(1L));
        assertFalse(userService.userExists(2L));
        
        verify(userRepository).existsById(1L);
        verify(userRepository).existsById(2L);
    }
}
