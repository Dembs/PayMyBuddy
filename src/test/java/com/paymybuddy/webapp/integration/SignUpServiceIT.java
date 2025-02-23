package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.repository.TransactionRepository;
import com.paymybuddy.webapp.service.SignUpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SignUpServiceIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SignUpService signUpService;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void registerNewUserTest() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("New User");
        userDTO.setEmail("newuser@example.com");
        userDTO.setPassword("Password123!");

        User createdUser = signUpService.registerNewUser(userDTO);

        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(userDTO.getEmail(), createdUser.getEmail());
        assertEquals(userDTO.getUsername(), createdUser.getRealUsername());

        assertNotNull(createdUser.getAccount());
        assertEquals(createdUser.getId(), createdUser.getAccount().getUser().getId());
    }

    @Test
    void registerNewUserErrorTest() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Test User");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("Password123!");
        signUpService.registerNewUser(userDTO);

        UserDTO duplicateDTO = new UserDTO();
        duplicateDTO.setUsername("Another User");
        duplicateDTO.setEmail("test@example.com");
        duplicateDTO.setPassword("AnotherPass123!");

        assertThrows(RuntimeException.class, () -> signUpService.registerNewUser(duplicateDTO));
        assertEquals(1, userRepository.count());
    }
}