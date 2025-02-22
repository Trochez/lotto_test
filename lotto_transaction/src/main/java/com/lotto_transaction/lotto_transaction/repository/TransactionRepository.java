package com.lotto_transaction.lotto_transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lotto_transaction.lotto_transaction.entity.Transaction;
import com.lotto_transaction.lotto_transaction.enumValues.TransactionStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCategoryId(Long category_id);
    List<Transaction> findByStatus(TransactionStatus status);
    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
