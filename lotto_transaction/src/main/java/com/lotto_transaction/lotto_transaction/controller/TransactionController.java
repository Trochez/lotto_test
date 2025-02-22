package com.lotto_transaction.lotto_transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.lotto_transaction.lotto_transaction.entity.Transaction;
import com.lotto_transaction.lotto_transaction.enumValues.TransactionStatus;
import com.lotto_transaction.lotto_transaction.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transacciones", description = "Operaciones relacionadas con las transacciones")

public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    @Operation(summary = "Obtiene todas las transacciones", description = "Permite consultar todas las transacciones")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transactions found");
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva transaccion", description = "Permite crear una nueva transaccion.")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {

        try {
            Transaction createdTransaction = transactionService.createTransaction(transaction);
            return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category ID does not exist.", e);
            }
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating transaction", e);
        }
    }

    @GetMapping("/category/{category_id}")
    @Operation(summary = "Obtener transacciones por categoría", description = "Devuelve una lista de todas las transacciones por la categoría especificada.")
    public ResponseEntity<List<Transaction>> getTransactionsByCategory_id(@PathVariable Long category_id) {

        List<Transaction> transactions = transactionService.getTransactionsByCategory_id(category_id);
        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transactions found for category");
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);

    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Obtener transacciones por estado", description = "Devuelve una lista de todas las transacciones por el estado especificado.")
    public ResponseEntity<List<Transaction>> getTransactionsByStatus(@PathVariable TransactionStatus status) {
        List<Transaction> transactions = transactionService.getTransactionsByStatus(status);
        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transactions found for status");
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Obtener transacciones por rango de fecha", description = "Devuelve una lista de todas las transacciones por el rango de fechas especificado.")
    public ResponseEntity<List<Transaction>> getTransactionsByDateRange(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        List<Transaction> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transactions found in date range");
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de transaccion", description = "Atualiza el estado de la transaccion especificada")
    public ResponseEntity<Transaction> updateTransactionStatus(@PathVariable Long id, @RequestParam TransactionStatus status) {
        try {
            Transaction updatedTransaction = transactionService.updateTransactionStatus(id, status);
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating transaction status", e);
        }
    }

    @GetMapping("/summary-by-category")
    @Operation(summary = "Obtener resumen de gastos", description = "Obtiene un resumen de gastos discriminados por categoria")
    public ResponseEntity<Map<String, Double>> getTotalExpensesByCategoryName() {
        Map<String, Double> summary = transactionService.getTotalExpensesByCategoryName();
        if (summary.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No expenses found");
        }

        return new ResponseEntity<>(summary, HttpStatus.OK);
    }
}