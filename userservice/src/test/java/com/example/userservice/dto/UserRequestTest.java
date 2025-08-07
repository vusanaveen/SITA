package com.example.userservice.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRequestTest {

    @Test
    void shouldSetAndGetAllFields() {
        UserRequest req = new UserRequest();
        req.setUsername("name");
        req.setPassword("pass1234");
        req.setEmail("a@b.com");

        assertEquals("name", req.getUsername());
        assertEquals("pass1234", req.getPassword());
        assertEquals("a@b.com", req.getEmail());
    }
}


