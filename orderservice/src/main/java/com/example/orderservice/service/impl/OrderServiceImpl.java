package com.example.orderservice.service.impl;

import com.example.orderservice.client.UserServiceClient;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.exception.InvalidUserException;
import com.example.orderservice.exception.ResourceNotFoundException;
import com.example.orderservice.exception.ValidationException;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of OrderService interface.
 * 
 * This class provides the business logic for order management operations
 * including CRUD operations, user validation, and data transformation.
 * 
 * @author Naveen V
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        log.info("Creating new order");
        
        validateOrderRequest(orderRequest);
        
        // Validate user exists
        if (!userServiceClient.userExists(orderRequest.getUserId())) {
            throw new InvalidUserException("User not found with ID: " + orderRequest.getUserId());
        }
        
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setProduct(orderRequest.getProduct());
        order.setQuantity(orderRequest.getQuantity());
        order.setPrice(orderRequest.getPrice());
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with ID: {}", savedOrder.getId());
        
        return mapToOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        log.info("Retrieving order with ID: {}", id);
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        
        log.info("Order retrieved successfully: {}", order.getProduct());
        return mapToOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        log.info("Retrieving all orders");
        
        List<Order> orders = orderRepository.findAll();
        log.info("Retrieved {} orders", orders.size());
        
        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        log.info("Retrieving orders for user ID: {}", userId);
        
        // Validate user exists first
        if (!userServiceClient.userExists(userId)) {
            throw new InvalidUserException("User not found with ID: " + userId);
        }
        
        List<Order> orders = orderRepository.findByUserId(userId);
        log.info("Retrieved {} orders for user ID: {}", orders.size(), userId);
        
        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        log.info("Updating order with ID: {}", id);
        
        validateOrderRequest(orderRequest);
        
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        
        // Validate user exists if userId is being changed
        if (!existingOrder.getUserId().equals(orderRequest.getUserId())) {
            if (!userServiceClient.userExists(orderRequest.getUserId())) {
                throw new InvalidUserException("User not found with ID: " + orderRequest.getUserId());
            }
        }
        
        existingOrder.setUserId(orderRequest.getUserId());
        existingOrder.setProduct(orderRequest.getProduct());
        existingOrder.setQuantity(orderRequest.getQuantity());
        existingOrder.setPrice(orderRequest.getPrice());
        
        Order updatedOrder = orderRepository.save(existingOrder);
        log.info("Order updated successfully: {}", updatedOrder.getProduct());
        
        return mapToOrderResponse(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        log.info("Deleting order with ID: {}", id);
        
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with ID: " + id);
        }
        
        orderRepository.deleteById(id);
        log.info("Order deleted successfully with ID: {}", id);
    }

    /**
     * Validate order request data.
     * 
     * @param orderRequest the order request to validate
     * @throws ValidationException if validation fails
     */
    private void validateOrderRequest(OrderRequest orderRequest) {
        if (orderRequest == null) {
            throw new ValidationException("Order request cannot be null");
        }
        
        if (orderRequest.getUserId() == null) {
            throw new ValidationException("User ID is required");
        }
        
        if (orderRequest.getProduct() == null || orderRequest.getProduct().trim().isEmpty()) {
            throw new ValidationException("Product is required");
        }
        
        if (orderRequest.getQuantity() == null || orderRequest.getQuantity() < 1) {
            throw new ValidationException("Quantity must be at least 1");
        }
        
        if (orderRequest.getPrice() == null) {
            throw new ValidationException("Price is required");
        }
        
        if (orderRequest.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Price must be positive");
        }
    }

    /**
     * Map Order entity to OrderResponse DTO.
     * 
     * @param order the order entity
     * @return the order response DTO
     */
    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getProduct(),
                order.getQuantity(),
                order.getPrice()
        );
    }
}
