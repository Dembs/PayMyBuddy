package com.paymybuddy.webapp.integration;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.service.TransactionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc

class TransactionServiceIT {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        sender = userRepository.findById(1).orElseThrow();
        receiver = userRepository.findById(2).orElseThrow();
    }

    @Test
    void transfertMoneyTest() {
        transactionService.processTransaction(
                sender.getId(),
                "TRANSFERT",
                100.0,
                "Test transfer",
                receiver.getId()
        );

        double senderBalance = transactionService.getUserBalance(sender.getId());
        double receiverBalance = transactionService.getUserBalance(receiver.getId());

        assertEquals(899.5, senderBalance); // 1000 (dépôt initial) - 100 - 0.5 (frais)
        assertEquals(1100.0, receiverBalance); // 1000 (dépôt initial) + 100
    }
    @Test
    void shouldProcessBankTransferIn() {
        transactionService.processTransaction(
                sender.getId(),
                "VIREMENT ENTRANT",
                500.0,
                "Dépôt bancaire",
                sender.getId()
        );

        double senderBalance = transactionService.getUserBalance(sender.getId());
        assertEquals(1500.0, senderBalance); // 1000 (initial) + 500
    }

    @Test
    void shouldProcessBankTransferOut() {
        transactionService.processTransaction(
                sender.getId(),
                "VIREMENT SORTANT",
                200.0,
                "Retrait bancaire",
                sender.getId()
        );

        double senderBalance = transactionService.getUserBalance(sender.getId());
        assertEquals(799.0, senderBalance); // 1000 (initial) - 200 - 1 (frais)
    }

    @Test
    void transfertMoneyErrorTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.processTransaction(
                    sender.getId(),
                    "TRANSFERT",
                    2000.0,  // montant supérieur au solde
                    "Test transfer",
                    receiver.getId()
            );
        });
    }
}