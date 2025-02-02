package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserService userService;

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

    public double getUserBalance(int userId){
        //select sum(amount) from transaction where sender = 1 or receiver = 1;
        List<Transaction> transactions = transactionRepository.findAllBySenderIdOrReceiverId(userId);
        double balance = 0.00;

        for (Transaction transaction : transactions) {
            if (transaction.getReceiver().getId() == userId) {
                // Pour les transferts entrants, on ajoute juste le montant sans frais
                balance += transaction.getAmount();
            } else if (transaction.getSender().getId() == userId) {
                // Pour les transferts sortants, on soustrait le montant et les frais
                balance += transaction.getAmount() - transaction.getFee();
            }
        }
        return balance;
    }
    public Map<String,Object> getTransactionByUser(int userId, int page, int size){
        Pageable pageable =  PageRequest.of(page,size);
        List<Transaction> transactions = transactionRepository.findBySenderIdOrReceiverId(userId, pageable);
        // Calculer le nombre total de transactions
        long totalTransactions = transactionRepository.countBySenderIdOrReceiverId(userId);
        int totalPages = (int) Math.ceil((double) totalTransactions / size);

        Map<String, Object> result = new HashMap<>();
        result.put("transactions", transactions);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);

        return result;
    }

    @Transactional
    public void processTransaction(int userId, String type, Double amount,
                                   String description, Integer receiverId) {
        User currentUser = userService.getUserById(userId);
        double balance = getUserBalance(userId);
        double totalAmount = amount * 1.005;

        // Vérification du solde pour les opérations sortantes
        if ((type.equals("VIREMENT SORTANT") || type.equals("TRANSFERT"))
                && totalAmount > balance) {
            throw new IllegalArgumentException("Solde insuffisant pour effectuer cette opération");
        }

        if(type.equals("TRANSFERT")){
        User receiver = userService.getUserById(receiverId);
        // Transaction sortante pour l'émetteur
        Transaction senderTransaction = new Transaction();
        senderTransaction.setType("TRANSFERT SORTANT");
        senderTransaction.setSender(currentUser);
        senderTransaction.setReceiver(receiver);
        senderTransaction.setDescription(description);
        senderTransaction.setAmount(-amount);
        senderTransaction.setDate(new Timestamp(System.currentTimeMillis()));
        saveTransaction(senderTransaction);

        // Transaction entrante pour le destinataire
        Transaction receiverTransaction = new Transaction();
        receiverTransaction.setType("TRANSFERT ENTRANT");
        receiverTransaction.setSender(currentUser);
        receiverTransaction.setReceiver(receiver);
        receiverTransaction.setDescription(description);
        receiverTransaction.setAmount(amount);
        receiverTransaction.setDate(new Timestamp(System.currentTimeMillis()));
        saveTransaction(receiverTransaction);

    } else if (type.contains("VIREMENT")) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setSender(currentUser);
        transaction.setReceiver(currentUser);
        transaction.setDescription(type);
        transaction.setAmount(type.equals("VIREMENT SORTANT") ? -amount : amount);
        transaction.setDate(new Timestamp(System.currentTimeMillis()));
        saveTransaction(transaction);
    }
    }
    }