package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.dto.UserDTO;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.SignUpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignUpControllerTest {

    @Mock
    private SignUpService signUpService;

    @Mock
    private Model model;

    @InjectMocks
    private SignUpController signUpController;

    private User testUser;
    private String testUsername;
    private String testEmail;
    private String testPassword;

    @BeforeEach
    void setUp() {
        testUsername = "testUser";
        testEmail = "test@test.com";
        testPassword = "password123";

        testUser = new User();
        testUser.setUsername(testUsername);
        testUser.setEmail(testEmail);
        testUser.setPassword(testPassword);
    }

    @Test
    void showSignupFormTest() {
        String viewName = signUpController.showSignupForm();

        assertEquals("signup", viewName);
    }

    @Test
    void processSignupTest() {
        when(signUpService.registerNewUser(any(UserDTO.class))).thenReturn(testUser);

        String viewName = signUpController.processSignup(
                testUsername,
                testEmail,
                testPassword,
                model
        );

        assertEquals("redirect:/login", viewName);
        verify(signUpService).registerNewUser(any(UserDTO.class));
    }

    @Test
    void processSignupErrorTest() {
        String errorMessage = "Email already in use";
        when(signUpService.registerNewUser(any(UserDTO.class)))
                .thenThrow(new RuntimeException(errorMessage));

        String viewName = signUpController.processSignup(
                testUsername,
                testEmail,
                testPassword,
                model
        );

        assertEquals("signup", viewName);
        verify(model).addAttribute("errorMessage", errorMessage);
    }
}