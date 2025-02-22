package com.lotto_transaction.lotto_transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.lotto_transaction.lotto_transaction.client.CategoryClient;
import com.lotto_transaction.lotto_transaction.client.dto.CategoryDTO;
import com.lotto_transaction.lotto_transaction.entity.Transaction;
import com.lotto_transaction.lotto_transaction.enumValues.TransactionStatus;
import com.lotto_transaction.lotto_transaction.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryClient categoryClient;

    /**
     * Obtiene todas las transacciones
     */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Crea una transaccion
     */
    public Transaction createTransaction(Transaction transaction) {
        try {
            categoryClient.validateCategoryExists(transaction.getCategoryId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category ID does not exist.");
        }
        return transactionRepository.save(transaction);
    }

    /**
     * Obtiene todas las transacciones por categoría
     */
    public List<Transaction> getTransactionsByCategory_id(Long category_id) {
        return transactionRepository.findByCategoryId(category_id);
    }

    /**
     * Obtiene todas las transacciones por estado
     */
    public List<Transaction> getTransactionsByStatus(TransactionStatus status) {
        return transactionRepository.findByStatus(status);
    }

    /**
     * Obtiene todas las transacciones en un rango de fechas
     */
    public List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByDateBetween(startDate, endDate);
    }

    /**
     * Actualiza el estado de una transacción
     */
    public Transaction updateTransactionStatus(Long id, TransactionStatus newStatus) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        transaction.setStatus(newStatus);
        return transactionRepository.save(transaction);
    }


    /**
     * Obtiene resumen de gastos agrupados por categoria
     */
    @SuppressWarnings("null")
    public Map<String, Double> getTotalExpensesByCategoryName() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> {
                            try {
                                CategoryDTO categoryDTO = categoryClient.getCategoryById(transaction.getCategoryId()).getBody();
                                return categoryDTO.getName();
                            } catch (Exception e) {
                                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving category name", e);
                            }
                        },
                        Collectors.summingDouble(t -> t.getAmount() != null ? t.getAmount().doubleValue() : 0.0)
                ));
    }
    
}
