package com.lotto_transaction.lotto_transaction.seeder.service;

import com.lotto_transaction.lotto_transaction.entity.Transaction;
import com.lotto_transaction.lotto_transaction.enumValues.TransactionStatus;
import com.lotto_transaction.lotto_transaction.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionSeederService {

    @Autowired
    private TransactionRepository transactionRepository;


    public List<Transaction> seedTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        
        Transaction t1 = new Transaction();
        t1.setCategoryId(1L);
        t1.setAmount(new BigDecimal("50.0"));
        t1.setCard_number(1234);
        t1.setDate(LocalDate.now());
        t1.setStatus(TransactionStatus.PENDIENTE);
        transactions.add(t1);

        Transaction t2 = new Transaction();
        t2.setCategoryId(2L);
        t2.setAmount(new BigDecimal("30.0"));
        t2.setCard_number(4569);
        t2.setDate(LocalDate.now());
        t2.setStatus(TransactionStatus.RECHAZADA);
        transactions.add(t2);

        Transaction t3 = new Transaction();
        t3.setCategoryId(3L);
        t3.setAmount(new BigDecimal("20.0"));
        t3.setCard_number(8855);
        t3.setDate(LocalDate.now());
        t3.setStatus(TransactionStatus.APROBADA);
        transactions.add(t3);

        Transaction t4 = new Transaction();
        t4.setCategoryId(3L);
        t4.setAmount(new BigDecimal("40.0"));
        t4.setCard_number(1145);
        t4.setDate(LocalDate.now());
        t4.setStatus(TransactionStatus.PENDIENTE);
        transactions.add(t4);

        Transaction t5 = new Transaction();
        t5.setCategoryId(4L);
        t5.setAmount(new BigDecimal("60.0"));
        t5.setCard_number(9823);
        t5.setDate(LocalDate.now());
        t5.setStatus(TransactionStatus.APROBADA);
        transactions.add(t5);

        return transactionRepository.saveAll(transactions);
    }
}
