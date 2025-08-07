package com.example.userservice.config;

import com.example.common.config.BaseOpenApiConfig;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for UserService.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig extends BaseOpenApiConfig {

    @Override
    protected String getServiceName() {
        return "User Service API";
    }

    @Override
    protected String getServiceDescription() {
        return "REST API for user management operations";
    }

    @Override
    protected String getServerUrl() {
        return "http://localhost:8081";
    }
}
