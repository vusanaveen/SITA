package com.example.orderservice.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderRequestTest {

    @Test
    void shouldSetAndGetAllFields() {
        OrderRequest req = new OrderRequest();
        req.setUserId(5L);
        req.setProduct("Phone");
        req.setQuantity(2);
        req.setPrice(new BigDecimal("499.99") );

        assertEquals(5L, req.getUserId());
        assertEquals("Phone", req.getProduct());
        assertEquals(2, req.getQuantity());
        assertEquals(new BigDecimal("499.99"), req.getPrice());
    }
}


