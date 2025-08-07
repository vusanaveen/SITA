package com.example.orderservice;

import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

/**
 * Main application class for OrderService.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@SpringBootApplication
@Slf4j
public class OrderServiceApplication {

    @Autowired(required = false)
    private OrderRepository orderRepository;

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    /**
     * Populates the database with sample data for testing.
     */
    @Bean
    public CommandLineRunner sampleData() {
        return args -> {
            if (orderRepository != null) {
                log.info("Populating database with sample data...");
                
                // Create sample orders
                Order order1 = new Order();
                order1.setUserId(1L);
                order1.setProduct("Laptop");
                order1.setQuantity(1);
                order1.setPrice(new BigDecimal("999.99"));
                orderRepository.save(order1);
                
                Order order2 = new Order();
                order2.setUserId(1L);
                order2.setProduct("Mouse");
                order2.setQuantity(2);
                order2.setPrice(new BigDecimal("25.50"));
                orderRepository.save(order2);
                
                Order order3 = new Order();
                order3.setUserId(2L);
                order3.setProduct("Keyboard");
                order3.setQuantity(1);
                order3.setPrice(new BigDecimal("75.00"));
                orderRepository.save(order3);
                
                log.info("Sample data populated successfully!");
            }
        };
    }
}
