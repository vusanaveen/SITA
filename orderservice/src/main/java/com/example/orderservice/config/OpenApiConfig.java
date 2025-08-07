package com.example.orderservice.config;

import com.example.common.config.BaseOpenApiConfig;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for OrderService.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig extends BaseOpenApiConfig {

    @Override
    protected String getServiceName() {
        return "Order Service API";
    }

    @Override
    protected String getServiceDescription() {
        return "REST API for order management operations";
    }

    @Override
    protected String getServerUrl() {
        return "http://localhost:8082";
    }
}
