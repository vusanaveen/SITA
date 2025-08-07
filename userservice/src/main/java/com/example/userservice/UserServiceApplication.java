package com.example.userservice;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main application class for UserService.
 * 
 * @author Senior Consultant
 * @version 1.0.0
 */
@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class UserServiceApplication {

    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    /**
     * Populates the database with sample data for testing.
     */
    @Bean
    public CommandLineRunner sampleData() {
        return args -> {
            log.info("Populating database with sample data...");
            
            // Create sample users
            User user1 = new User();
            user1.setUsername("john_doe");
            user1.setPassword("password123");
            user1.setEmail("john@example.com");
            userRepository.save(user1);
            
            User user2 = new User();
            user2.setUsername("jane_smith");
            user2.setPassword("password456");
            user2.setEmail("jane@example.com");
            userRepository.save(user2);
            
            User user3 = new User();
            user3.setUsername("bob_wilson");
            user3.setPassword("password789");
            user3.setEmail("bob@example.com");
            userRepository.save(user3);
            
            log.info("Sample data populated successfully!");
        };
    }
}
