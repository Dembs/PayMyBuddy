package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.service.TransactionService;
import com.paymybuddy.webapp.service.UserService;
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
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private TransactionController transactionController;

    private User testUser;
    private Map<String, Object> pageData;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@test.com");

        pageData = new HashMap<>();
        pageData.put("transactions", new ArrayList<>());
        pageData.put("currentPage", 0);
        pageData.put("totalPages", 1);

    }

    @Test
    void showTransferPageTest() {

        when(userService.getUserById(1)).thenReturn(testUser);
        when(transactionService.getUserBalance(1)).thenReturn(1000.0);
        when(transactionService.getTransactionByUser(eq(1), anyInt(), eq(5)))
                .thenReturn(pageData);

        String viewName = transactionController.showTransferPage(testUser, model, 0);

        assertEquals("transfert", viewName);
        verify(model).addAttribute("user", testUser);
        verify(model).addAttribute("balance", 1000.0);
        verify(model).addAttribute("transactions", pageData.get("transactions"));
        verify(model).addAttribute("currentPage", pageData.get("currentPage"));
        verify(model).addAttribute("totalPages", pageData.get("totalPages"));
    }

    @Test
    void addTransactionTest() {
        String viewName = transactionController.addTransaction(
                testUser,
                "TRANSFERT",
                100.0,
                "Test transfer",
                2,
                redirectAttributes
        );

        assertEquals("redirect:/transfert", viewName);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Transaction effectuée avec succès");
    }

    @Test
    void addTransactionErrorTest() {
        doThrow(new IllegalArgumentException("Solde insuffisant pour effectuer cette opération"))
                .when(transactionService).processTransaction(
                        anyInt(), anyString(), anyDouble(), anyString(), anyInt());

        String viewName = transactionController.addTransaction(
                testUser,
                "TRANSFERT",
                100.0,
                "Test transfer",
                2,
                redirectAttributes
        );

        assertEquals("redirect:/transfert", viewName);
        verify(redirectAttributes).addFlashAttribute("errorMessage",
                "Solde insuffisant pour effectuer cette opération");
    }
}