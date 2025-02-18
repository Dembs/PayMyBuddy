package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.AccountRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account testAccount;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@test.com");

        testAccount = new Account();
        testAccount.setUser(testUser);
    }

    @Test
    void saveAccountTest() {
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        Account savedAccount = accountService.saveAccount(testAccount);

        assertNotNull(savedAccount);
        assertEquals(testUser, savedAccount.getUser());
        verify(accountRepository).save(testAccount);
    }

    @Test
    void saveAccountErrorTest() {
        Account accountWithoutUser = new Account();

        assertThrows(IllegalArgumentException.class, () ->
                accountService.saveAccount(accountWithoutUser)
        );
    }

}