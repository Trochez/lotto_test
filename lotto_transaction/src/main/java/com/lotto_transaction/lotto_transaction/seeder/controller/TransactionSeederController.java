package com.lotto_transaction.lotto_transaction.seeder.controller;

import com.lotto_transaction.lotto_transaction.entity.Transaction;
import com.lotto_transaction.lotto_transaction.seeder.service.TransactionSeederService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transaction-seeder")
public class TransactionSeederController {

    @Autowired
    private  TransactionSeederService transactionSeederService;


    @GetMapping("/seed")
    @Operation(summary = "Inserta datos de prueba.", description = "Inserta datos de prueba en la base de datos de transacciones.")
    public ResponseEntity<List<Transaction>> seedTransactions() {
        List<Transaction> seededTransactions = transactionSeederService.seedTransactions();
        return new ResponseEntity<>(seededTransactions, HttpStatus.OK);
    }
}