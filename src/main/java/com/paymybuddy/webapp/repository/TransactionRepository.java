package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    // Find transactions by sender
    List<Transaction> findBySenderId(int senderId);

    // Find transactions by receiver
    List<Transaction> findByReceiverId(int receiverId);

    // Find transactions by type
    List<Transaction> findByType(String type);
}