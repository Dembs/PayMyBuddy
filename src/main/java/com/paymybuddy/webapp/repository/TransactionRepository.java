package com.paymybuddy.webapp.repository;

import com.paymybuddy.webapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findBySenderId(int senderId);

    List<Transaction> findByReceiverId(int receiverId);

    @Query("SELECT t FROM Transaction t WHERE (t.sender.id = :userId AND (t.type = 'TRANSFERT SORTANT' OR t.type LIKE 'VIREMENT%')) " +
                    "OR (t.receiver.id = :userId AND t.type = 'TRANSFERT ENTRANT') " +
                    "ORDER BY t.date DESC")
    List<Transaction> findBySenderIdOrReceiverId(@Param("userId") int userId, Pageable pageable);
    // Méthode pour le calcul du solde (sans pagination)
    @Query("SELECT t FROM Transaction t WHERE (t.sender.id = :userId AND (t.type = 'TRANSFERT SORTANT' OR t.type LIKE 'VIREMENT%')) " +
            "OR (t.receiver.id = :userId AND t.type = 'TRANSFERT ENTRANT') ")
    List<Transaction> findAllBySenderIdOrReceiverId(@Param("userId") int userId);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE (t.sender.id = :userId AND (t.type = 'TRANSFERT SORTANT' OR t.type LIKE 'VIREMENT%')) " +
            "OR (t.receiver.id = :userId AND t.type = 'TRANSFERT ENTRANT')")
    long countBySenderIdOrReceiverId(@Param("userId") int userId);
}