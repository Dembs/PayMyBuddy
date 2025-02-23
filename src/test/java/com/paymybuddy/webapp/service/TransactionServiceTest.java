package com.paymybuddy.webapp.service;

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
    private Transaction depositTransaction;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(1);
        sender.setEmail("sender@test.com");

        receiver = new User();
        receiver.setId(2);
        receiver.setEmail("receiver@test.com");

        // Dépôt initial
        depositTransaction = new Transaction();
        depositTransaction.setSender(sender);
        depositTransaction.setType("VIREMENT ENTRANT");
        depositTransaction.setAmount(1000.0);
        depositTransaction.setFee(0.0);
        depositTransaction.setDate(new Timestamp(System.currentTimeMillis()));

    }

    @Test
    void processTransactionTest() {
        double amount = 100.0;

        when(userService.getUserById(1)).thenReturn(sender);
        when(userService.getUserById(2)).thenReturn(receiver);
        when(transactionRepository.findAllBySenderIdOrReceiverId(1))
                .thenReturn(Arrays.asList(depositTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        // When
        transactionService.processTransaction(1, "TRANSFERT", amount, "Test transfer", 2);
        // Then
        //vérifie l'utilisation du save chez le sender et le receiver
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    void processTransactionBankTransferOutTest() {
        double amount = 200.0;
        double fee = amount * 0.005;

        when(userService.getUserById(1)).thenReturn(sender);
        when(transactionRepository.findAllBySenderIdOrReceiverId(1))
                .thenReturn(Arrays.asList(depositTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        transactionService.processTransaction(1, "VIREMENT SORTANT", amount, "Ma Banque", 1);

        verify(transactionRepository).save(argThat(transaction ->
                transaction.getType().equals("VIREMENT SORTANT") &&
                        transaction.getAmount() == -amount &&
                        transaction.getFee() == fee &&
                        transaction.getSender().getId() == 1 &&
                        transaction.getReceiver().getId() == 1
        ));
    }
    @Test
    void processTransactionWithAmountSuperiorTest() {
        double amount = 2000.0;

        when(userService.getUserById(1)).thenReturn(sender);
        when(transactionRepository.findAllBySenderIdOrReceiverId(1))
                .thenReturn(Arrays.asList(depositTransaction));

        assertThrows(IllegalArgumentException.class, () ->
                transactionService.processTransaction(1, "TRANSFERT", amount, "Test transfer", 2)
        );
    }
    @Test
    void processTransactionWithNegativeAmountTest() {
        assertThrows(IllegalArgumentException.class, () ->
                transactionService.processTransaction(1, "TRANSFERT", -100.0, "Test", 2)
        );
    }

    @Test
    void getTransactionsByUserTest() {
        //On crée 3 transactions en plus
        List<Transaction> transactions = Arrays.asList(testTransaction, new Transaction(), new Transaction());

        when(transactionRepository.findBySenderIdOrReceiverId(eq(1), any(PageRequest.class)))
                .thenReturn(transactions);
        when(transactionRepository.countBySenderIdOrReceiverId(1)).thenReturn(3L); //3 transactions au total

        Map<String, Object> result = transactionService.getTransactionByUser(1, 0, 2);//On teste 2 éléments par page avec 0 en page initialle

        assertNotNull(result);
        assertEquals(transactions, result.get("transactions"));
        assertEquals(0, result.get("currentPage"));
        assertEquals(2, result.get("totalPages"));
    }

    @Test
    void getUserBalanceTest() {

        Transaction transfer = new Transaction();
        transfer.setType("TRANSFERT SORTANT");
        transfer.setAmount(-100.0);
        transfer.setFee(0.5);

        when(transactionRepository.findAllBySenderIdOrReceiverId(1))
                .thenReturn(Arrays.asList(depositTransaction, transfer));

        double balance = transactionService.getUserBalance(1);

        assertEquals(899.5, balance); // 1000 - 100 - 0.5
    }

}