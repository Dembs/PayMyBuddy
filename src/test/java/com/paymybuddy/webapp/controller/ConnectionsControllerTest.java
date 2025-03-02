package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.ConnectionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectionsControllerTest {

    @Mock
    private ConnectionsService connectionsService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private ConnectionsController connectionsController;

    private User testUser;
    private Map<String, Object> pageData;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@test.com");

        pageData = new HashMap<>();
        pageData.put("connections", new ArrayList<>());
        pageData.put("currentPage", 0);
        pageData.put("totalPages", 1);
    }

    @Test
    void showConnectionsPageTest() {
        when(connectionsService.getConnectionsByUser(any(User.class), anyInt(), eq(5)))
                .thenReturn(pageData);

        String viewName = connectionsController.showConnectionsPage(testUser, model, 0);

        assertEquals("/connections", viewName);
        verify(model).addAttribute("connections", pageData.get("connections"));
        verify(model).addAttribute("currentPage", pageData.get("currentPage"));
        verify(model).addAttribute("totalPages", pageData.get("totalPages"));
    }

    @Test
    void processConnectionTest() {
        String viewName = connectionsController.processConnection(
                testUser,
                "friend@test.com",
                redirectAttributes,
                model
        );

        assertEquals("redirect:/connections", viewName);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Relation rajoutée avec succes");
    }

    @Test
    void processConnectionErrorTest() {
        String errorMessage = "Contact déjà rajouté";
        doThrow(new RuntimeException(errorMessage))
                .when(connectionsService).addConnection(any(User.class), anyString());
        when(connectionsService.getConnectionsByUser(any(User.class), anyInt(), eq(5)))
                .thenReturn(pageData);

        String viewName = connectionsController.processConnection(
                testUser,
                "friend@test.com",
                redirectAttributes,
                model
        );

        assertEquals("connections", viewName);
        verify(model, atLeastOnce()).addAttribute("errorMessage", errorMessage);
        verify(model).addAttribute("connections", pageData.get("connections"));
        verify(model).addAttribute("currentPage", pageData.get("currentPage"));
        verify(model).addAttribute("totalPages", pageData.get("totalPages"));
    }
}