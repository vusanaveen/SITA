package com.example.userservice.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    void shouldSetAndGetAllFields() {
        User user = new User();
        user.setId(7L);
        user.setUsername("u");
        user.setPassword("p");
        user.setEmail("e@e.com");

        assertEquals(7L, user.getId());
        assertEquals("u", user.getUsername());
        assertEquals("p", user.getPassword());
        assertEquals("e@e.com", user.getEmail());
    }
}


