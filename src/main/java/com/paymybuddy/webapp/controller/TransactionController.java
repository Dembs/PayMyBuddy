package com.paymybuddy.webapp.controller;

import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        try {
            Transaction savedTransaction = transactionService.saveTransaction(transaction);
            return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable int id, @RequestBody Transaction updatedTransaction) {
        try {
            Transaction savedTransaction = transactionService.updateTransaction(id, updatedTransaction);
            return new ResponseEntity<>(savedTransaction, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable int id) {
        try {
            Transaction transaction = transactionService.getTransactionById(id);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable int id) {
        try {
            transactionService.deleteTransaction(id);
            return new ResponseEntity<>("Transaction supprimée avec succès", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<Transaction>> getTransactionsBySender(@PathVariable int senderId) {
        List<Transaction> transactions = transactionService.getTransactionsBySender(senderId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<Transaction>> getTransactionsByReceiver(@PathVariable int receiverId) {
        List<Transaction> transactions = transactionService.getTransactionsByReceiver(receiverId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}