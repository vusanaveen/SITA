package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;

import java.util.List;

/**
 * Service interface for Order business logic.
 * 
 * This interface defines the business operations for order management
 * including CRUD operations and user validation.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
public interface OrderService {

    /**
     * Create a new order.
     * 
     * @param orderRequest the order data to create
     * @return the created order response
     */
    OrderResponse createOrder(OrderRequest orderRequest);

    /**
     * Get an order by ID.
     * 
     * @param id the order ID
     * @return the order response
     * @throws ResourceNotFoundException if order not found
     */
    OrderResponse getOrderById(Long id);

    /**
     * Get all orders.
     * 
     * @return list of all orders
     */
    List<OrderResponse> getAllOrders();

    /**
     * Get orders by user ID.
     * 
     * @param userId the user ID
     * @return list of orders for the user
     */
    List<OrderResponse> getOrdersByUserId(Long userId);

    /**
     * Update an order by ID.
     * 
     * @param id the order ID
     * @param orderRequest the updated order data
     * @return the updated order response
     * @throws ResourceNotFoundException if order not found
     */
    OrderResponse updateOrder(Long id, OrderRequest orderRequest);

    /**
     * Delete an order by ID.
     * 
     * @param id the order ID
     * @throws ResourceNotFoundException if order not found
     */
    void deleteOrder(Long id);
}
