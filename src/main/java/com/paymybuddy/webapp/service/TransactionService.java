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

    public double getUserBalance(int userId){
        //select sum(amount) from transaction where sender = 1 or receiver = 1;
        List<Transaction> transactions = transactionRepository.findAllBySenderIdOrReceiverId(userId);
        double balance = 0.00;

        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
            balance -= transaction.getFee();
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
        double fee = amount *0.005;
        double totalAmount = amount + fee;

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
        senderTransaction.setFee(fee);
        senderTransaction.setDate(new Timestamp(System.currentTimeMillis()));
        saveTransaction(senderTransaction);

        // Transaction entrante pour le destinataire
        Transaction receiverTransaction = new Transaction();
        receiverTransaction.setType("TRANSFERT ENTRANT");
        receiverTransaction.setSender(currentUser);
        receiverTransaction.setReceiver(receiver);
        receiverTransaction.setDescription(description);
        receiverTransaction.setAmount(amount);
        receiverTransaction.setFee(0.0);
        receiverTransaction.setDate(new Timestamp(System.currentTimeMillis()));
        saveTransaction(receiverTransaction);

    } else if (type.contains("VIREMENT")) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setSender(currentUser);
        transaction.setReceiver(currentUser);
        transaction.setDescription(type);
        transaction.setAmount(type.equals("VIREMENT SORTANT") ? -amount : amount);
        transaction.setFee(type.equals("VIREMENT SORTANT") ? fee : 0.0);
        transaction.setDate(new Timestamp(System.currentTimeMillis()));
        saveTransaction(transaction);
    }
    }
    }