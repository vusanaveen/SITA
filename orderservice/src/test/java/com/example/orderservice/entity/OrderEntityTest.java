package com.example.orderservice.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderEntityTest {

    @Test
    void shouldSetAndGetAllFields() {
        Order order = new Order();
        order.setId(10L);
        order.setUserId(3L);
        order.setProduct("Tablet");
        order.setQuantity(1);
        order.setPrice(new BigDecimal("299.00"));

        assertEquals(10L, order.getId());
        assertEquals(3L, order.getUserId());
        assertEquals("Tablet", order.getProduct());
        assertEquals(1, order.getQuantity());
        assertEquals(new BigDecimal("299.00"), order.getPrice());
    }
}


