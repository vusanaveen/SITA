package com.example.userservice.controller;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.common.exception.ResourceNotFoundException;
import com.example.common.exception.ValidationException;
import com.example.userservice.service.UserService;
import com.example.userservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for UserController.
 * 
 * Tests all controller endpoints with @WebMvcTest and 100% code coverage.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@WebMvcTest(UserController.class)
@Import(com.example.userservice.exception.GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // Ensure @WebMvcTest can instantiate the application context without data layer
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequest testUserRequest;
    private UserResponse testUserResponse;

    @BeforeEach
    void setUp() {
        testUserRequest = new UserRequest();
        testUserRequest.setUsername("testuser");
        testUserRequest.setPassword("password123");
        testUserRequest.setEmail("test@example.com");

        testUserResponse = new UserResponse();
        testUserResponse.setId(1L);
        testUserResponse.setUsername("testuser");
        testUserResponse.setEmail("test@example.com");
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() throws Exception {
        when(userService.createUser(any(UserRequest.class))).thenReturn(testUserResponse);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService).createUser(any(UserRequest.class));
    }

    @Test
    @DisplayName("Should return 400 when creating user with invalid data")
    void shouldReturn400WhenCreatingUserWithInvalidData() throws Exception {
        UserRequest invalidRequest = new UserRequest();
        invalidRequest.setUsername(""); 
        invalidRequest.setPassword("123");
        invalidRequest.setEmail("invalid-email");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserRequest.class));
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void shouldGetUserByIdSuccessfully() throws Exception {
        when(userService.getUserById(1L)).thenReturn(testUserResponse);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("Should return 404 when user not found by ID")
    void shouldReturn404WhenUserNotFoundById() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new ResourceNotFoundException("User not found with ID: 1"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("User not found with ID: 1"));

        verify(userService).getUserById(1L);
    }

    @Test
    @DisplayName("Should get all users successfully")
    void shouldGetAllUsersSuccessfully() throws Exception {
        UserResponse user2 = new UserResponse();
        user2.setId(2L);
        user2.setUsername("testuser2");
        user2.setEmail("test2@example.com");
        
        List<UserResponse> users = Arrays.asList(testUserResponse, user2);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("testuser2"));

        verify(userService).getAllUsers();
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() throws Exception {
        when(userService.updateUser(eq(1L), any(UserRequest.class))).thenReturn(testUserResponse);

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService).updateUser(eq(1L), any(UserRequest.class));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent user")
    void shouldReturn404WhenUpdatingNonExistentUser() throws Exception {
        when(userService.updateUser(eq(1L), any(UserRequest.class)))
                .thenThrow(new ResourceNotFoundException("User not found with ID: 1"));

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("User not found with ID: 1"));

        verify(userService).updateUser(eq(1L), any(UserRequest.class));
    }

    @Test
    @DisplayName("Should return 400 when updating user with invalid data")
    void shouldReturn400WhenUpdatingUserWithInvalidData() throws Exception {
        UserRequest invalidRequest = new UserRequest();
        invalidRequest.setUsername("");
        invalidRequest.setPassword("123");
        invalidRequest.setEmail("invalid-email");

        mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(any(Long.class), any(UserRequest.class));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent user")
    void shouldReturn404WhenDeletingNonExistentUser() throws Exception {
        doThrow(new ResourceNotFoundException("User not found with ID: 1"))
                .when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("User not found with ID: 1"));

        verify(userService).deleteUser(1L);
    }

    @Test
    @DisplayName("Should check if user exists successfully")
    void shouldCheckIfUserExistsSuccessfully() throws Exception {
        when(userService.userExists(1L)).thenReturn(true);
        when(userService.userExists(2L)).thenReturn(false);

        mockMvc.perform(get("/users/1/exists"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/2/exists"))
                .andExpect(status().isNotFound());

        verify(userService).userExists(1L);
        verify(userService).userExists(2L);
    }

    @Test
    @DisplayName("Should return 400 when validation exception occurs")
    void shouldReturn400WhenValidationExceptionOccurs() throws Exception {
        when(userService.createUser(any(UserRequest.class)))
                .thenThrow(new ValidationException("Invalid input data provided"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Invalid input data provided"));

        verify(userService).createUser(any(UserRequest.class));
    }

    @Test
    @DisplayName("Should return 500 when internal server exception occurs")
    void shouldReturn500WhenInternalServerExceptionOccurs() throws Exception {
        when(userService.createUser(any(UserRequest.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("An unexpected error occurred"));

        verify(userService).createUser(any(UserRequest.class));
    }
}
