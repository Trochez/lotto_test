package com.lotto_transaction.lotto_transaction.seeder.service;

import com.lotto_transaction.lotto_transaction.entity.Transaction;
import com.lotto_transaction.lotto_transaction.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        transactions.add(t1);

        Transaction t2 = new Transaction();
        t2.setCategoryId(2L);
        t2.setAmount(new BigDecimal("30.0"));
        transactions.add(t2);

        Transaction t3 = new Transaction();
        t3.setCategoryId(3L);
        t3.setAmount(new BigDecimal("20.0"));
        transactions.add(t3);

        Transaction t4 = new Transaction();
        t4.setCategoryId(3L);
        t4.setAmount(new BigDecimal("40.0"));
        transactions.add(t4);

        Transaction t5 = new Transaction();
        t5.setCategoryId(4L);
        t5.setAmount(new BigDecimal("60.0"));
        transactions.add(t5);

        return transactionRepository.saveAll(transactions);
    }
}
