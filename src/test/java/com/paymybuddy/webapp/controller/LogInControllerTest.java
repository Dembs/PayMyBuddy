package com.paymybuddy.webapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


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