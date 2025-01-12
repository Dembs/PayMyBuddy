package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction saveTransaction(Transaction transaction) {
        // Set current timestamp if not provided
        if (transaction.getDate() == null) {
            transaction.setDate(new Timestamp(System.currentTimeMillis()));
        }
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(int id, Transaction updatedTransaction) {
        Transaction existingTransaction = transactionRepository.findById(id)
                                                               .orElseThrow(() -> new IllegalArgumentException("Transaction non trouvée avec l'ID : " + id));

        // Update specific fields
        if (updatedTransaction.getDescription() != null) {
            existingTransaction.setDescription(updatedTransaction.getDescription());
        }
        if (updatedTransaction.getAmount() != 0) {
            existingTransaction.setAmount(updatedTransaction.getAmount());
        }
        if (updatedTransaction.getType() != null) {
            existingTransaction.setType(updatedTransaction.getType());
        }

        return transactionRepository.save(existingTransaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(int id) {
        return transactionRepository.findById(id)
                                    .orElseThrow(() -> new IllegalArgumentException("Transaction non trouvée avec l'ID : " + id));
    }

    public void deleteTransaction(int id) {
        if (!transactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Transaction non trouvée avec l'ID : " + id);
        }
        transactionRepository.deleteById(id);
    }

    // Additional methods for specific queries
    public List<Transaction> getTransactionsBySender(int senderId) {
        return transactionRepository.findBySenderId(senderId);
    }

    public List<Transaction> getTransactionsByReceiver(int receiverId) {
        return transactionRepository.findByReceiverId(receiverId);
    }
}