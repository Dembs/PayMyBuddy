package com.paymybuddy.webapp.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDTOTest {
    @Test
    void createUserTest() {

        UserDTO userDTO = new UserDTO("test@test.com", "password", "testUser");

        assertEquals("test@test.com", userDTO.getEmail());
        assertEquals("password", userDTO.getPassword());
        assertEquals("testUser", userDTO.getUsername());
    }
}
