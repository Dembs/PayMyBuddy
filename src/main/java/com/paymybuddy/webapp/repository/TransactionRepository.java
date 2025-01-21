package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findBySenderId(int senderId);

    List<Transaction> findByReceiverId(int receiverId);

    List<Transaction> findByType(String type);

    List<Transaction> findBySenderIdOrReceiverId(int senderId, int receiverId);
}