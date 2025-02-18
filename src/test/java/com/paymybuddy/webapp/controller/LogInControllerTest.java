package com.paymybuddy.webapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class LogInControllerTest {

    @InjectMocks
    private LogInController logincontroller;

    @Test
    void showLoginPageTest() {

        String viewName = logincontroller.showLoginForm();

        assertEquals("login", viewName);
    }

}