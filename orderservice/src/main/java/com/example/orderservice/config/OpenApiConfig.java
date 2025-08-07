package com.example.orderservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for OrderService.
 * 
 * Provides API documentation and interactive testing interface.
 * 
 * @author Senior Consultant
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orderServiceOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8082");
        devServer.setDescription("Development server for OrderService");

        Contact contact = new Contact();
        contact.setName("Senior Consultant");
        contact.setEmail("consultant@example.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("OrderService API")
                .version("1.0.0")
                .description("This API provides CRUD operations for Order entities. " +
                        "It manages order information including user ID, product, quantity, and price. " +
                        "The service validates user existence with UserService before creating orders. " +
                        "It uses H2 in-memory database for data persistence.")
                .contact(contact)
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}
