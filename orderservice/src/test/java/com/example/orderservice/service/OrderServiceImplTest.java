package com.example.orderservice.service;

import com.example.orderservice.client.UserServiceClient;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.common.exception.InvalidUserException;
import com.example.common.exception.ResourceNotFoundException;
import com.example.common.exception.ValidationException;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for OrderServiceImpl.
 * 
 * Tests all service methods with mocked dependencies and 100% code coverage.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OrderServiceImpl Tests")
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder;
    private OrderRequest testOrderRequest;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setUserId(1L);
        testOrder.setProduct("Laptop");
        testOrder.setQuantity(1);
        testOrder.setPrice(new BigDecimal("999.99"));

        testOrderRequest = new OrderRequest();
        testOrderRequest.setUserId(1L);
        testOrderRequest.setProduct("Laptop");
        testOrderRequest.setQuantity(1);
        testOrderRequest.setPrice(new BigDecimal("999.99"));
    }

    @Test
    @DisplayName("Should throw ValidationException when price is not positive")
    void shouldThrowValidationExceptionWhenPriceIsNotPositive() {
        OrderRequest bad = new OrderRequest();
        bad.setUserId(1L);
        bad.setProduct("Item");
        bad.setQuantity(1);
        bad.setPrice(new java.math.BigDecimal("-1.00"));

        com.example.common.exception.ValidationException ex = assertThrows(com.example.common.exception.ValidationException.class,
                () -> orderService.createOrder(bad));
        assertEquals("Price must be positive", ex.getMessage());
    }

    @Test
    @DisplayName("Should update order without changing userId successfully")
    void shouldUpdateOrderWithoutChangingUserIdSuccessfully() {
        com.example.orderservice.entity.Order existing = new com.example.orderservice.entity.Order();
        existing.setId(1L);
        existing.setUserId(1L);
        existing.setProduct("Old");
        existing.setQuantity(1);
        existing.setPrice(new java.math.BigDecimal("10.00"));

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));

        OrderRequest update = new OrderRequest();
        update.setUserId(1L); // unchanged
        update.setProduct("New");
        update.setQuantity(2);
        update.setPrice(new java.math.BigDecimal("20.00"));

        when(orderRepository.save(any(com.example.orderservice.entity.Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderResponse resp = orderService.updateOrder(1L, update);

        assertEquals(1L, resp.getUserId());
        assertEquals("New", resp.getProduct());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(com.example.orderservice.entity.Order.class));
        // userServiceClient.userExists should NOT be called when userId unchanged
        verify(userServiceClient, never()).userExists(anyLong());
    }

    @Test
    @DisplayName("Should create order successfully")
    void shouldCreateOrderSuccessfully() {
        when(userServiceClient.userExists(1L)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        OrderResponse result = orderService.createOrder(testOrderRequest);

        assertNotNull(result);
        assertEquals(testOrder.getId(), result.getId());
        assertEquals(testOrder.getUserId(), result.getUserId());
        assertEquals(testOrder.getProduct(), result.getProduct());
        assertEquals(testOrder.getQuantity(), result.getQuantity());
        assertEquals(testOrder.getPrice(), result.getPrice());
        
        verify(userServiceClient).userExists(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw InvalidUserException when user does not exist")
    void shouldThrowInvalidUserExceptionWhenUserDoesNotExist() {
        when(userServiceClient.userExists(1L)).thenReturn(false);

        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("User not found with ID: 1", exception.getMessage());
        
        verify(userServiceClient).userExists(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when order request is null")
    void shouldThrowValidationExceptionWhenOrderRequestIsNull() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(null));
        assertEquals("Order request cannot be null", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when user ID is null")
    void shouldThrowValidationExceptionWhenUserIdIsNull() {
        testOrderRequest.setUserId(null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("User ID is required", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when product is null")
    void shouldThrowValidationExceptionWhenProductIsNull() {
        testOrderRequest.setProduct(null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("Product is required", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when product is empty")
    void shouldThrowValidationExceptionWhenProductIsEmpty() {
        testOrderRequest.setProduct("");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("Product is required", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when quantity is null")
    void shouldThrowValidationExceptionWhenQuantityIsNull() {
        testOrderRequest.setQuantity(null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("Quantity must be at least 1", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when quantity is less than 1")
    void shouldThrowValidationExceptionWhenQuantityIsLessThanOne() {
        // Given
        testOrderRequest.setQuantity(0);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("Quantity must be at least 1", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when price is null")
    void shouldThrowValidationExceptionWhenPriceIsNull() {
        // Given
        testOrderRequest.setPrice(null);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("Price is required", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when price is zero")
    void shouldThrowValidationExceptionWhenPriceIsZero() {
        testOrderRequest.setPrice(BigDecimal.ZERO);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("Price must be positive", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when price is negative")
    void shouldThrowValidationExceptionWhenPriceIsNegative() {
        testOrderRequest.setPrice(new BigDecimal("-10.00"));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("Price must be positive", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should get order by ID successfully")
    void shouldGetOrderByIdSuccessfully() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        OrderResponse result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(testOrder.getId(), result.getId());
        assertEquals(testOrder.getUserId(), result.getUserId());
        assertEquals(testOrder.getProduct(), result.getProduct());
        
        verify(orderRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when order not found by ID")
    void shouldThrowResourceNotFoundExceptionWhenOrderNotFoundById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.getOrderById(1L));
        assertEquals("Order not found with ID: 1", exception.getMessage());
        
        verify(orderRepository).findById(1L);
    }

    @Test
    @DisplayName("Should get all orders successfully")
    void shouldGetAllOrdersSuccessfully() {
        Order order2 = new Order();
        order2.setId(2L);
        order2.setUserId(2L);
        order2.setProduct("Mouse");
        order2.setQuantity(2);
        order2.setPrice(new BigDecimal("29.99"));
        
        List<Order> orders = Arrays.asList(testOrder, order2);
        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderResponse> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testOrder.getId(), result.get(0).getId());
        assertEquals(order2.getId(), result.get(1).getId());
        
        verify(orderRepository).findAll();
    }

    @Test
    @DisplayName("Should get orders by user ID successfully")
    void shouldGetOrdersByUserIdSuccessfully() {
        Order order2 = new Order();
        order2.setId(2L);
        order2.setUserId(1L);
        order2.setProduct("Keyboard");
        order2.setQuantity(1);
        order2.setPrice(new BigDecimal("89.99"));
        
        List<Order> orders = Arrays.asList(testOrder, order2);
        when(orderRepository.findByUserId(1L)).thenReturn(orders);
        when(userServiceClient.userExists(1L)).thenReturn(true);

        List<OrderResponse> result = orderService.getOrdersByUserId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testOrder.getId(), result.get(0).getId());
        assertEquals(order2.getId(), result.get(1).getId());
        
        verify(orderRepository).findByUserId(1L);
        verify(userServiceClient).userExists(1L);
    }

    @Test
    @DisplayName("Should throw InvalidUserException when fetching orders for non-existent user")
    void shouldThrowInvalidUserExceptionWhenGettingOrdersByUserIdIfUserDoesNotExist() {
        when(userServiceClient.userExists(1L)).thenReturn(false);

        InvalidUserException ex = assertThrows(InvalidUserException.class,
                () -> orderService.getOrdersByUserId(1L));
        assertEquals("User not found with ID: 1", ex.getMessage());

        verify(userServiceClient).userExists(1L);
        verify(orderRepository, never()).findByUserId(anyLong());
    }

    @Test
    @DisplayName("Should update order successfully")
    void shouldUpdateOrderSuccessfully() {
        OrderRequest updateRequest = new OrderRequest();
        updateRequest.setUserId(2L);
        updateRequest.setProduct("Updated Laptop");
        updateRequest.setQuantity(2);
        updateRequest.setPrice(new BigDecimal("1299.99"));
        
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(userServiceClient.userExists(2L)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        OrderResponse result = orderService.updateOrder(1L, updateRequest);

        assertNotNull(result);
        assertEquals(testOrder.getId(), result.getId());
        
        verify(orderRepository).findById(1L);
        verify(userServiceClient).userExists(2L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent order")
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.updateOrder(1L, testOrderRequest));
        assertEquals("Order not found with ID: 1", exception.getMessage());
        
        verify(orderRepository).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw InvalidUserException when updating with non-existent user")
    void shouldThrowInvalidUserExceptionWhenUpdatingWithNonExistentUser() {
        OrderRequest updateRequest = new OrderRequest();
        updateRequest.setUserId(999L);
        updateRequest.setProduct("Laptop");
        updateRequest.setQuantity(1);
        updateRequest.setPrice(new BigDecimal("999.99"));
        
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(userServiceClient.userExists(999L)).thenReturn(false);

        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> orderService.updateOrder(1L, updateRequest));
        assertEquals("User not found with ID: 999", exception.getMessage());
        
        verify(orderRepository).findById(1L);
        verify(userServiceClient).userExists(999L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should delete order successfully")
    void shouldDeleteOrderSuccessfully() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(orderRepository).existsById(1L);
        verify(orderRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent order")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentOrder() {
        when(orderRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.deleteOrder(1L));
        assertEquals("Order not found with ID: 1", exception.getMessage());
        
        verify(orderRepository).existsById(1L);
        verify(orderRepository, never()).deleteById(any(Long.class));
    }
}
