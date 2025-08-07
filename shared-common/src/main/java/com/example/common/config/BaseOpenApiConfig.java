package com.example.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Base OpenAPI configuration for all services.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@Configuration
public abstract class BaseOpenApiConfig {

    /**
     * Get the service name for OpenAPI documentation.
     * 
     * @return the service name
     */
    protected abstract String getServiceName();

    /**
     * Get the service description for OpenAPI documentation.
     * 
     * @return the service description
     */
    protected abstract String getServiceDescription();

    /**
     * Get the server URL for OpenAPI documentation.
     * 
     * @return the server URL
     */
    protected abstract String getServerUrl();

    /**
     * Configure OpenAPI documentation.
     * 
     * @return the OpenAPI configuration
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(getServiceName())
                        .description(getServiceDescription())
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Naveen Vusa")
                                .email("naveen@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url(getServerUrl())
                                .description("Development Server")
                ));
    }
}
