package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionService transactionService;

    private User sender;
    private User receiver;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(1);
        sender.setEmail("sender@test.com");

        receiver = new User();
        receiver.setId(2);
        receiver.setEmail("receiver@test.com");

        // Transaction de dépôt initial pour avoir un solde suffisant
        Transaction depositTransaction = new Transaction();
        depositTransaction.setSender(sender);
        depositTransaction.setType("VIREMENT ENTRANT");
        depositTransaction.setAmount(1000.0);
        depositTransaction.setFee(0.0);
        depositTransaction.setDate(new Timestamp(System.currentTimeMillis()));

        testTransaction = new Transaction();
        testTransaction.setId(1);
        testTransaction.setSender(sender);
        testTransaction.setReceiver(receiver);
        testTransaction.setAmount(100.0);
        testTransaction.setDescription("Test transaction");
        testTransaction.setType("TRANSFERT");
        testTransaction.setFee(0.5);
        testTransaction.setDate(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void processTransactionTest() {
        double amount = 100.0;
        Transaction initialDeposit = new Transaction();
        initialDeposit.setType("VIREMENT ENTRANT");
        initialDeposit.setAmount(1000.0);
        initialDeposit.setFee(0.0);

        when(userService.getUserById(1)).thenReturn(sender);
        when(userService.getUserById(2)).thenReturn(receiver);
        when(transactionRepository.findAllBySenderIdOrReceiverId(1))
                .thenReturn(Arrays.asList(initialDeposit)); // Retourne le dépôt initial
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        // When
        transactionService.processTransaction(1, "TRANSFERT", amount, "Test transfer", 2);

        // Then
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    void processTransactionFailTest() {

        double amount = 2000.0;
        Transaction initialDeposit = new Transaction();
        initialDeposit.setType("VIREMENT ENTRANT");
        initialDeposit.setAmount(1000.0);
        initialDeposit.setFee(0.0);

        when(userService.getUserById(1)).thenReturn(sender);
        when(transactionRepository.findAllBySenderIdOrReceiverId(1))
                .thenReturn(Arrays.asList(initialDeposit));

        assertThrows(IllegalArgumentException.class, () ->
                transactionService.processTransaction(1, "TRANSFERT", amount, "Test transfer", 2)
        );
    }

    @Test
    void getTransactionByUserTest() {

        List<Transaction> transactions = Arrays.asList(testTransaction, new Transaction(), new Transaction());
        when(transactionRepository.findBySenderIdOrReceiverId(eq(1), any(PageRequest.class)))
                .thenReturn(transactions);
        when(transactionRepository.countBySenderIdOrReceiverId(1)).thenReturn(3L);

        Map<String, Object> result = transactionService.getTransactionByUser(1, 0, 2);

        assertNotNull(result);
        assertEquals(transactions, result.get("transactions"));
        assertEquals(0, result.get("currentPage"));
        assertEquals(2, result.get("totalPages"));
    }

    @Test
    void getUserBalanceTest() {
        Transaction deposit = new Transaction();
        deposit.setType("VIREMENT ENTRANT");
        deposit.setAmount(1000.0);
        deposit.setFee(0.0);

        Transaction transfer = new Transaction();
        transfer.setType("TRANSFERT SORTANT");
        transfer.setAmount(-100.0);
        transfer.setFee(0.5);

        when(transactionRepository.findAllBySenderIdOrReceiverId(1))
                .thenReturn(Arrays.asList(deposit, transfer));

        double balance = transactionService.getUserBalance(1);

        assertEquals(899.5, balance); // 1000 - 100 - 0.5
    }

    @Test
    void saveTransactionTest() {

        Transaction transaction = new Transaction();
        transaction.setAmount(100.0);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction savedTransaction = transactionService.saveTransaction(transaction);

        assertNotNull(savedTransaction.getDate());
        verify(transactionRepository).save(transaction);
    }
}