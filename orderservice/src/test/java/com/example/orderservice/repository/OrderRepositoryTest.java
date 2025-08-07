package com.example.orderservice.repository;

import com.example.orderservice.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for OrderRepository.
 * 
 * Tests all repository methods with @DataJpaTest and 100% code coverage.
 * 
 * @author Naveen Vusa
 * @version 1.0.0
 */
@DataJpaTest
@DisplayName("OrderRepository Tests")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order testOrder1;
    private Order testOrder2;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        
        testOrder1 = new Order();
        testOrder1.setUserId(1L);
        testOrder1.setProduct("Laptop");
        testOrder1.setQuantity(1);
        testOrder1.setPrice(new BigDecimal("999.99"));

        testOrder2 = new Order();
        testOrder2.setUserId(2L);
        testOrder2.setProduct("Mouse");
        testOrder2.setQuantity(2);
        testOrder2.setPrice(new BigDecimal("29.99"));
    }

    @Test
    @DisplayName("Should save order successfully")
    void shouldSaveOrderSuccessfully() {
        Order savedOrder = orderRepository.save(testOrder1);

        assertNotNull(savedOrder.getId());
        assertEquals(testOrder1.getUserId(), savedOrder.getUserId());
        assertEquals(testOrder1.getProduct(), savedOrder.getProduct());
        assertEquals(testOrder1.getQuantity(), savedOrder.getQuantity());
        assertEquals(testOrder1.getPrice(), savedOrder.getPrice());
    }

    @Test
    @DisplayName("Should find order by ID")
    void shouldFindOrderById() {
        Order savedOrder = orderRepository.save(testOrder1);

        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());

        assertTrue(foundOrder.isPresent());
        assertEquals(savedOrder.getId(), foundOrder.get().getId());
        assertEquals(savedOrder.getProduct(), foundOrder.get().getProduct());
    }

    @Test
    @DisplayName("Should return empty when order not found by ID")
    void shouldReturnEmptyWhenOrderNotFoundById() {
        Optional<Order> foundOrder = orderRepository.findById(999L);

        assertFalse(foundOrder.isPresent());
    }

    @Test
    @DisplayName("Should find all orders")
    void shouldFindAllOrders() {
        orderRepository.save(testOrder1);
        orderRepository.save(testOrder2);

        List<Order> orders = orderRepository.findAll();

        assertEquals(2, orders.size());
        assertTrue(orders.stream().anyMatch(order -> order.getProduct().equals("Laptop")));
        assertTrue(orders.stream().anyMatch(order -> order.getProduct().equals("Mouse")));
    }

    @Test
    @DisplayName("Should find orders by user ID")
    void shouldFindOrdersByUserId() {
        orderRepository.save(testOrder1);
        orderRepository.save(testOrder2);

        List<Order> orders = orderRepository.findByUserId(1L);

        assertEquals(1, orders.size());
        assertEquals("Laptop", orders.get(0).getProduct());
        assertEquals(1L, orders.get(0).getUserId());
    }

    @Test
    @DisplayName("Should return empty list when no orders found for user ID")
    void shouldReturnEmptyListWhenNoOrdersFoundForUserId() {
        List<Order> orders = orderRepository.findByUserId(999L);

        assertTrue(orders.isEmpty());
    }

    @Test
    @DisplayName("Should check if orders exist for user ID")
    void shouldCheckIfOrdersExistForUserId() {
        orderRepository.save(testOrder1);

        assertTrue(orderRepository.existsByUserId(1L));
        assertFalse(orderRepository.existsByUserId(999L));
    }

    @Test
    @DisplayName("Should count orders by user ID")
    void shouldCountOrdersByUserId() {
        Order order3 = new Order();
        order3.setUserId(1L);
        order3.setProduct("Keyboard");
        order3.setQuantity(1);
        order3.setPrice(new BigDecimal("89.99"));
        
        orderRepository.save(testOrder1);
        orderRepository.save(order3);

        assertEquals(2, orderRepository.countByUserId(1L));
        assertEquals(0, orderRepository.countByUserId(999L));
    }

    @Test
    @DisplayName("Should update order successfully")
    void shouldUpdateOrderSuccessfully() {
        Order savedOrder = orderRepository.save(testOrder1);
        savedOrder.setProduct("Updated Laptop");
        savedOrder.setPrice(new BigDecimal("1299.99"));

        Order updatedOrder = orderRepository.save(savedOrder);

        assertEquals("Updated Laptop", updatedOrder.getProduct());
        assertEquals(new BigDecimal("1299.99"), updatedOrder.getPrice());
        assertEquals(savedOrder.getId(), updatedOrder.getId());
    }

    @Test
    @DisplayName("Should delete order successfully")
    void shouldDeleteOrderSuccessfully() {
        Order savedOrder = orderRepository.save(testOrder1);

        orderRepository.deleteById(savedOrder.getId());

        assertFalse(orderRepository.existsById(savedOrder.getId()));
    }

    @Test
    @DisplayName("Should check if order exists by ID")
    void shouldCheckIfOrderExistsById() {
        Order savedOrder = orderRepository.save(testOrder1);

        assertTrue(orderRepository.existsById(savedOrder.getId()));
        assertFalse(orderRepository.existsById(999L));
    }

    @Test
    @DisplayName("Should count orders")
    void shouldCountOrders() {
        orderRepository.save(testOrder1);
        orderRepository.save(testOrder2);

        long count = orderRepository.count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should delete all orders")
    void shouldDeleteAllOrders() {
        orderRepository.save(testOrder1);
        orderRepository.save(testOrder2);

        orderRepository.deleteAll();

        assertEquals(0, orderRepository.count());
    }
}
