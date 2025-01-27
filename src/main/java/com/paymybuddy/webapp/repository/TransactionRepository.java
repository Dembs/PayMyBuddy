package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findBySenderId(int senderId);

    List<Transaction> findByReceiverId(int receiverId);


    @Query("Select t from Transaction t where t.sender.id = :userId OR t.receiver.id = :userId order by t.date desc")
    List<Transaction> findBySenderIdOrReceiverIdOrderByDateDesc(@Param("userId") int userId);
}