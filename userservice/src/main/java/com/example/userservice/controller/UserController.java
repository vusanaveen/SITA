package com.example.userservice.controller;

import com.example.userservice.dto.ErrorResponse;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for User management operations.
 * 
 * This controller provides REST endpoints for user CRUD operations
 * with proper HTTP status codes and validation.
 * 
 * @author Senior Consultant
 * @version 1.0.0
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "APIs for managing user operations")
public class UserController {

    private final UserService userService;

    /**
     * Create a new user.
     * 
     * @param userRequest the user data
     * @return the created user with 201 status
     */
    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class), 
                examples = @ExampleObject(value = """
                    {
                      "error": "Invalid input data provided",
                      "status": 400,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Database connection failed",
                      "status": 500,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """)))
    })
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("POST /users - Creating user: {}", userRequest.getUsername());
        UserResponse createdUser = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Get a user by ID.
     * 
     * @param id the user ID
     * @return the user with 200 status
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "User not found with ID: 123",
                      "status": 404,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Database connection failed",
                      "status": 500,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """)))
    })
    public ResponseEntity<UserResponse> getUserById(@Parameter(description = "User ID") @PathVariable Long id) {
        log.info("GET /users/{} - Retrieving user", id);
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Get all users.
     * 
     * @return list of all users with 200 status
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Database connection failed",
                      "status": 500,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """)))
    })
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("GET /users - Retrieving all users");
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Update a user by ID.
     * 
     * @param id the user ID
     * @param userRequest the updated user data
     * @return the updated user with 200 status
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update user by ID", description = "Updates an existing user with new information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Invalid input data provided",
                      "status": 400,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """))),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "User not found with ID: 123",
                      "status": 404,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Database connection failed",
                      "status": 500,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """)))
    })
    public ResponseEntity<UserResponse> updateUser(@Parameter(description = "User ID") @PathVariable Long id, 
                                                 @Valid @RequestBody UserRequest userRequest) {
        log.info("PUT /users/{} - Updating user", id);
        UserResponse updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete a user by ID.
     * 
     * @param id the user ID
     * @return 204 No Content status
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID", description = "Deletes a user by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "User not found with ID: 123",
                      "status": 404,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Database connection failed",
                      "status": 500,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """)))
    })
    public ResponseEntity<Void> deleteUser(@Parameter(description = "User ID") @PathVariable Long id) {
        log.info("DELETE /users/{} - Deleting user", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if a user exists by ID.
     * 
     * @param id the user ID
     * @return 200 OK if user exists, 404 Not Found otherwise
     */
    @GetMapping("/{id}/exists")
    @Operation(summary = "Check if user exists", description = "Checks if a user exists by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User exists"),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "User not found with ID: 123",
                      "status": 404,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "error": "Database connection failed",
                      "status": 500,
                      "timestamp": "2025-08-07T21:00:00Z"
                    }
                    """)))
    })
    public ResponseEntity<Void> checkUserExists(@Parameter(description = "User ID") @PathVariable Long id) {
        log.info("GET /users/{}/exists - Checking if user exists", id);
        boolean exists = userService.userExists(id);
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
