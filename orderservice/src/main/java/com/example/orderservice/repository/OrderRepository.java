package com.example.orderservice.repository;

import com.example.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Order entity.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find all orders by user ID.
     * 
     * @param userId the user ID to search for
     * @return list of orders for the user
     */
    List<Order> findByUserId(Long userId);

    /**
     * Check if orders exist for a user.
     * 
     * @param userId the user ID to check
     * @return true if orders exist, false otherwise
     */
    boolean existsByUserId(Long userId);

    /**
     * Count orders by user ID.
     * 
     * @param userId the user ID to count orders for
     * @return number of orders for the user
     */
    long countByUserId(Long userId);
}
