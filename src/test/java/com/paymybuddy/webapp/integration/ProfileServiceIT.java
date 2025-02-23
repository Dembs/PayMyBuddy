package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProfileServiceIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private final String INITIAL_PASSWORD = "password123";

    @BeforeEach
    void setUp() {
        testUser = userRepository.findByEmail("test@test.com").orElseThrow();
    }

    @Test
    void testUpdateEmail() {
        String newEmail = "newemail" + System.currentTimeMillis() + "@test.com";
        userService.updateEmail(testUser.getId(), newEmail);

        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertEquals(newEmail, updatedUser.getEmail());
    }

    @Test
    void testUpdateUsername() {
        String newUsername = "newUsername" + System.currentTimeMillis();
        userService.updateUsername(testUser.getId(), newUsername);

        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertEquals(newUsername, updatedUser.getRealUsername());
    }

    @Test
    void testUpdatePassword() {
        String newPassword = "newPassword123";

        userService.updatePassword(testUser.getId(), INITIAL_PASSWORD, newPassword);

        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertTrue(passwordEncoder.matches(newPassword, updatedUser.getPassword()));
    }

    @Test
    void testUpdatePasswordWithWrongCurrentPassword() {
        String wrongPassword = "wrongPassword123@";
        String newPassword = "newPassword123@";

        assertThrows(RuntimeException.class, () ->
                userService.updatePassword(testUser.getId(), wrongPassword, newPassword)
        );
    }
}