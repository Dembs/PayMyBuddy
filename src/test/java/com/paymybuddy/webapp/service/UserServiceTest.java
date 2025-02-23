package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("oldUsername");
        testUser.setPassword("encodedOldPassword");
    }

    @Test
    void updateUsernameTest() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUsername("newUsername")).thenReturn(false);

        userService.updateUsername(1, "newUsername");

        verify(userRepository).save(testUser);
        assertNotNull(testUser.getRealUsername());
        assertEquals("newUsername", testUser.getRealUsername());
        assertNotEquals("oldUsername", testUser.getRealUsername());
    }

    @Test
    void updateUsernameErrorTest() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUsername("newUsername")).thenReturn(true);


        assertThrows(RuntimeException.class, () ->
                userService.updateUsername(1, "newUsername")
        );
    }
    @Test
    void updateEmailTest() {

        String newEmail = "new@test.com";
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(newEmail)).thenReturn(false);

        userService.updateEmail(1, newEmail);

        verify(userRepository).save(testUser);
        assertEquals(newEmail, testUser.getEmail());
    }

    @Test
    void updatePasswordTest() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("currentPassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userService.updatePassword(1, "currentPassword", "newPassword");

        verify(userRepository).save(testUser);
        verify(passwordEncoder).encode("newPassword");
    }

    @Test
    void updatePasswordShortTest() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        assertThrows(IllegalArgumentException.class, () ->
                userService.updatePassword(1, "current", "short")
        );
    }

    @Test
    void updatePasswordMissmatchTest() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", "encodedOldPassword")).thenReturn(false);

        assertThrows(RuntimeException.class, () ->
                userService.updatePassword(1, "wrongPassword", "newPassword")
        );
    }
    @Test
    void UserNotFoundTest() {
        String nonExistentEmail = "nonexistent@test.com";
        when(userRepository.findByEmail(nonExistentEmail))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userService.loadUserByUsername(nonExistentEmail)
        );
        verify(userRepository).findByEmail(nonExistentEmail);
    }
}