package com.example.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user response data.
 * 
 * This class represents the data transfer object used for
 * returning user data in REST API responses.
 * 
 * @author Senior Consultant
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User response data")
public class UserResponse {

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;
    
    @Schema(description = "Username of the user", example = "john_doe")
    private String username;
    
    @Schema(description = "Email address of the user", example = "john@example.com")
    private String email;
}
