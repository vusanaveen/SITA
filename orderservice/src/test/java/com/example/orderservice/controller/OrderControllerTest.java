package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.exception.InvalidUserException;
import com.example.orderservice.exception.ResourceNotFoundException;
import com.example.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for OrderController.
 * 
 * Tests all controller endpoints with @WebMvcTest and 100% code coverage.
 * 
 * @author Senior Consultant
 * @version 1.0.0
 */
@WebMvcTest(OrderController.class)
@DisplayName("OrderController Tests")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderRequest testOrderRequest;
    private OrderResponse testOrderResponse;

    @BeforeEach
    void setUp() {
        testOrderRequest = new OrderRequest();
        testOrderRequest.setUserId(1L);
        testOrderRequest.setProduct("Laptop");
        testOrderRequest.setQuantity(1);
        testOrderRequest.setPrice(new BigDecimal("999.99"));

        testOrderResponse = new OrderResponse();
        testOrderResponse.setId(1L);
        testOrderResponse.setUserId(1L);
        testOrderResponse.setProduct("Laptop");
        testOrderResponse.setQuantity(1);
        testOrderResponse.setPrice(new BigDecimal("999.99"));
    }

    @Test
    @DisplayName("Should create order successfully")
    void shouldCreateOrderSuccessfully() throws Exception {
        // Given
        when(orderService.createOrder(any(OrderRequest.class))).thenReturn(testOrderResponse);

        // When & Then
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.product").value("Laptop"))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.price").value(999.99));

        verify(orderService).createOrder(any(OrderRequest.class));
    }

    @Test
    @DisplayName("Should return 400 when creating order with invalid data")
    void shouldReturn400WhenCreatingOrderWithInvalidData() throws Exception {
        // Given
        OrderRequest invalidRequest = new OrderRequest();
        invalidRequest.setUserId(null); // Invalid user ID
        invalidRequest.setProduct(""); // Invalid product
        invalidRequest.setQuantity(0); // Invalid quantity
        invalidRequest.setPrice(BigDecimal.ZERO); // Invalid price

        // When & Then
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(orderService, never()).createOrder(any(OrderRequest.class));
    }

    @Test
    @DisplayName("Should get order by ID successfully")
    void shouldGetOrderByIdSuccessfully() throws Exception {
        // Given
        when(orderService.getOrderById(1L)).thenReturn(testOrderResponse);

        // When & Then
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.product").value("Laptop"))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.price").value(999.99));

        verify(orderService).getOrderById(1L);
    }

    @Test
    @DisplayName("Should return 404 when order not found by ID")
    void shouldReturn404WhenOrderNotFoundById() throws Exception {
        // Given
        when(orderService.getOrderById(1L)).thenThrow(new ResourceNotFoundException("Order not found with ID: 1"));

        // When & Then
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                .andExpect(jsonPath("$.message").value("Order not found with ID: 1"));

        verify(orderService).getOrderById(1L);
    }

    @Test
    @DisplayName("Should get all orders successfully")
    void shouldGetAllOrdersSuccessfully() throws Exception {
        // Given
        OrderResponse order2 = new OrderResponse();
        order2.setId(2L);
        order2.setUserId(2L);
        order2.setProduct("Mouse");
        order2.setQuantity(2);
        order2.setPrice(new BigDecimal("29.99"));
        
        List<OrderResponse> orders = Arrays.asList(testOrderResponse, order2);
        when(orderService.getAllOrders()).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].product").value("Laptop"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].product").value("Mouse"));

        verify(orderService).getAllOrders();
    }

    @Test
    @DisplayName("Should get orders by user ID successfully")
    void shouldGetOrdersByUserIdSuccessfully() throws Exception {
        // Given
        OrderResponse order2 = new OrderResponse();
        order2.setId(2L);
        order2.setUserId(1L);
        order2.setProduct("Keyboard");
        order2.setQuantity(1);
        order2.setPrice(new BigDecimal("89.99"));
        
        List<OrderResponse> orders = Arrays.asList(testOrderResponse, order2);
        when(orderService.getOrdersByUserId(1L)).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].product").value("Laptop"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].product").value("Keyboard"));

        verify(orderService).getOrdersByUserId(1L);
    }

    @Test
    @DisplayName("Should update order successfully")
    void shouldUpdateOrderSuccessfully() throws Exception {
        // Given
        when(orderService.updateOrder(eq(1L), any(OrderRequest.class))).thenReturn(testOrderResponse);

        // When & Then
        mockMvc.perform(put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.product").value("Laptop"))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.price").value(999.99));

        verify(orderService).updateOrder(eq(1L), any(OrderRequest.class));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent order")
    void shouldReturn404WhenUpdatingNonExistentOrder() throws Exception {
        // Given
        when(orderService.updateOrder(eq(1L), any(OrderRequest.class)))
                .thenThrow(new ResourceNotFoundException("Order not found with ID: 1"));

        // When & Then
        mockMvc.perform(put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrderRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource Not Found"));

        verify(orderService).updateOrder(eq(1L), any(OrderRequest.class));
    }

    @Test
    @DisplayName("Should return 400 when updating order with invalid data")
    void shouldReturn400WhenUpdatingOrderWithInvalidData() throws Exception {
        // Given
        OrderRequest invalidRequest = new OrderRequest();
        invalidRequest.setUserId(null); // Invalid user ID
        invalidRequest.setProduct(""); // Invalid product
        invalidRequest.setQuantity(0); // Invalid quantity
        invalidRequest.setPrice(BigDecimal.ZERO); // Invalid price

        // When & Then
        mockMvc.perform(put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(orderService, never()).updateOrder(any(Long.class), any(OrderRequest.class));
    }

    @Test
    @DisplayName("Should delete order successfully")
    void shouldDeleteOrderSuccessfully() throws Exception {
        // Given
        doNothing().when(orderService).deleteOrder(1L);

        // When & Then
        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderService).deleteOrder(1L);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent order")
    void shouldReturn404WhenDeletingNonExistentOrder() throws Exception {
        // Given
        doThrow(new ResourceNotFoundException("Order not found with ID: 1"))
                .when(orderService).deleteOrder(1L);

        // When & Then
        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource Not Found"));

        verify(orderService).deleteOrder(1L);
    }

    @Test
    @DisplayName("Should return 400 when invalid user exception occurs")
    void shouldReturn400WhenInvalidUserExceptionOccurs() throws Exception {
        // Given
        when(orderService.createOrder(any(OrderRequest.class)))
                .thenThrow(new InvalidUserException("User not found with ID: 999"));

        // When & Then
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Invalid User"))
                .andExpect(jsonPath("$.message").value("User not found with ID: 999"));

        verify(orderService).createOrder(any(OrderRequest.class));
    }

    @Test
    @DisplayName("Should return 500 when internal server exception occurs")
    void shouldReturn500WhenInternalServerExceptionOccurs() throws Exception {
        // Given
        when(orderService.createOrder(any(OrderRequest.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrderRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"));

        verify(orderService).createOrder(any(OrderRequest.class));
    }
}
