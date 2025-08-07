package com.example.orderservice.service;

import com.example.orderservice.client.UserServiceClient;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.exception.InvalidUserException;
import com.example.orderservice.exception.ResourceNotFoundException;
import com.example.orderservice.exception.ValidationException;
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
 * @author Senior Consultant
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
    @DisplayName("Should create order successfully")
    void shouldCreateOrderSuccessfully() {
        // Given
        when(userServiceClient.userExists(1L)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        OrderResponse result = orderService.createOrder(testOrderRequest);

        // Then
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
        // Given
        when(userServiceClient.userExists(1L)).thenReturn(false);

        // When & Then
        InvalidUserException exception = assertThrows(InvalidUserException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("User not found with ID: 1", exception.getMessage());
        
        verify(userServiceClient).userExists(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when order request is null")
    void shouldThrowValidationExceptionWhenOrderRequestIsNull() {
        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(null));
        assertEquals("Order request cannot be null", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when user ID is null")
    void shouldThrowValidationExceptionWhenUserIdIsNull() {
        // Given
        testOrderRequest.setUserId(null);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("User ID is required", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when product is null")
    void shouldThrowValidationExceptionWhenProductIsNull() {
        // Given
        testOrderRequest.setProduct(null);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("Product is required", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when product is empty")
    void shouldThrowValidationExceptionWhenProductIsEmpty() {
        // Given
        testOrderRequest.setProduct("");

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("Product is required", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when quantity is null")
    void shouldThrowValidationExceptionWhenQuantityIsNull() {
        // Given
        testOrderRequest.setQuantity(null);

        // When & Then
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
        assertEquals("Price must be positive", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when price is zero")
    void shouldThrowValidationExceptionWhenPriceIsZero() {
        // Given
        testOrderRequest.setPrice(BigDecimal.ZERO);

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("Price must be positive", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ValidationException when price is negative")
    void shouldThrowValidationExceptionWhenPriceIsNegative() {
        // Given
        testOrderRequest.setPrice(new BigDecimal("-10.00"));

        // When & Then
        ValidationException exception = assertThrows(ValidationException.class,
                () -> orderService.createOrder(testOrderRequest));
        assertEquals("Price must be positive", exception.getMessage());
        
        verify(userServiceClient, never()).userExists(any(Long.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should get order by ID successfully")
    void shouldGetOrderByIdSuccessfully() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // When
        OrderResponse result = orderService.getOrderById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testOrder.getId(), result.getId());
        assertEquals(testOrder.getUserId(), result.getUserId());
        assertEquals(testOrder.getProduct(), result.getProduct());
        
        verify(orderRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when order not found by ID")
    void shouldThrowResourceNotFoundExceptionWhenOrderNotFoundById() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.getOrderById(1L));
        assertEquals("Order not found with ID: 1", exception.getMessage());
        
        verify(orderRepository).findById(1L);
    }

    @Test
    @DisplayName("Should get all orders successfully")
    void shouldGetAllOrdersSuccessfully() {
        // Given
        Order order2 = new Order();
        order2.setId(2L);
        order2.setUserId(2L);
        order2.setProduct("Mouse");
        order2.setQuantity(2);
        order2.setPrice(new BigDecimal("29.99"));
        
        List<Order> orders = Arrays.asList(testOrder, order2);
        when(orderRepository.findAll()).thenReturn(orders);

        // When
        List<OrderResponse> result = orderService.getAllOrders();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testOrder.getId(), result.get(0).getId());
        assertEquals(order2.getId(), result.get(1).getId());
        
        verify(orderRepository).findAll();
    }

    @Test
    @DisplayName("Should get orders by user ID successfully")
    void shouldGetOrdersByUserIdSuccessfully() {
        // Given
        Order order2 = new Order();
        order2.setId(2L);
        order2.setUserId(1L);
        order2.setProduct("Keyboard");
        order2.setQuantity(1);
        order2.setPrice(new BigDecimal("89.99"));
        
        List<Order> orders = Arrays.asList(testOrder, order2);
        when(orderRepository.findByUserId(1L)).thenReturn(orders);

        // When
        List<OrderResponse> result = orderService.getOrdersByUserId(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testOrder.getId(), result.get(0).getId());
        assertEquals(order2.getId(), result.get(1).getId());
        
        verify(orderRepository).findByUserId(1L);
    }

    @Test
    @DisplayName("Should update order successfully")
    void shouldUpdateOrderSuccessfully() {
        // Given
        OrderRequest updateRequest = new OrderRequest();
        updateRequest.setUserId(2L);
        updateRequest.setProduct("Updated Laptop");
        updateRequest.setQuantity(2);
        updateRequest.setPrice(new BigDecimal("1299.99"));
        
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(userServiceClient.userExists(2L)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        OrderResponse result = orderService.updateOrder(1L, updateRequest);

        // Then
        assertNotNull(result);
        assertEquals(testOrder.getId(), result.getId());
        
        verify(orderRepository).findById(1L);
        verify(userServiceClient).userExists(2L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent order")
    void shouldThrowResourceNotFoundExceptionWhenUpdatingNonExistentOrder() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.updateOrder(1L, testOrderRequest));
        assertEquals("Order not found with ID: 1", exception.getMessage());
        
        verify(orderRepository).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw InvalidUserException when updating with non-existent user")
    void shouldThrowInvalidUserExceptionWhenUpdatingWithNonExistentUser() {
        // Given
        OrderRequest updateRequest = new OrderRequest();
        updateRequest.setUserId(999L);
        updateRequest.setProduct("Laptop");
        updateRequest.setQuantity(1);
        updateRequest.setPrice(new BigDecimal("999.99"));
        
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(userServiceClient.userExists(999L)).thenReturn(false);

        // When & Then
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
        // Given
        when(orderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1L);

        // When
        orderService.deleteOrder(1L);

        // Then
        verify(orderRepository).existsById(1L);
        verify(orderRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent order")
    void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentOrder() {
        // Given
        when(orderRepository.existsById(1L)).thenReturn(false);

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.deleteOrder(1L));
        assertEquals("Order not found with ID: 1", exception.getMessage());
        
        verify(orderRepository).existsById(1L);
        verify(orderRepository, never()).deleteById(any(Long.class));
    }
}
