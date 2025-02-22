package com.lotto_transaction.lotto_transaction.controller;

import com.lotto_transaction.lotto_transaction.entity.Transaction;
import com.lotto_transaction.lotto_transaction.enumValues.TransactionStatus;
import com.lotto_transaction.lotto_transaction.service.TransactionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal(100.75));
        transaction.setStatus(TransactionStatus.PENDIENTE);
        transaction.setDate(LocalDate.now());
    }

    @SuppressWarnings("null")
    @Test
    void testGetAllTransactions_Success() throws Exception {

        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());

        // Simulamos que el servicio liste las transacciones exitosamente
        when(transactionService.getAllTransactions()).thenReturn(transactions);

        // Act
        ResponseEntity<List<Transaction>> response = transactionController.getAllTransactions();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        // Verificamos que se llamó al método createTransaction del servicio
        verify(transactionService, times(1)).getAllTransactions();

    }

    @Test
    void testGetAllTransactions_Controller_NoContent() throws Exception {
        // Simulamos que el servicio no liste ninguna transaccion
        when(transactionService.getAllTransactions()).thenReturn(List.of());

        //Verificamos que se lanza una ResponseStatusException al llamar al método
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.getAllTransactions();
        });

        // Verificamos que el código HTTP sea 404 (NOT_FOUND)
        ResponseStatusException responseException = (ResponseStatusException) exception;
        assertEquals(HttpStatus.NOT_FOUND.value(), responseException.getStatusCode().value());
        assertEquals("No transactions found", responseException.getReason());

        // Verificamos que se llamó al método getAllTransactions del servicio
        verify(transactionService, times(1)).getAllTransactions();

    }

    @SuppressWarnings("null")
    @Test
    void testCreateTransaction_Success() throws Exception {

        // Simulamos que el servicio crea la transaccion exitosamente
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);

        // Act
        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(new BigDecimal(100.75), response.getBody().getAmount());

        // Verificamos que se llamó al método createTransaction del servicio
        verify(transactionService, times(1)).createTransaction(transaction);
    }

    @Test
    void testCreateTransaction_Failure() {
        //Simulamos un fallo al crear la transacción
        when(transactionService.createTransaction(any(Transaction.class)))
                .thenThrow(new RuntimeException("Error creating transaction"));

        //Verificamos que se lanza una ResponseStatusException al llamar al método
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.createTransaction(null);
        });

        // Verificamos que el código HTTP sea 500 (INTERNAL_SERVER_ERROR)
        ResponseStatusException responseException = (ResponseStatusException) exception;
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseException.getStatusCode().value());
        assertEquals("Error creating transaction", responseException.getReason());

        // Verificamos que el servicio fue llamado una vez
        verify(transactionService, times(1)).createTransaction(null);
    }

    @Test
    void testCreateTransaction_CategoryNotFound() throws Exception {

        //Simulamos que no encuentra el id de la categoria
        when(transactionService.createTransaction(any(Transaction.class)))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Category ID does not exist."));

        //Verificamos que se lanza una ResponseStatusException al llamar al método
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.createTransaction(transaction);
        });

        // Verificamos que el código HTTP sea 500 (INTERNAL_SERVER_ERROR)
        ResponseStatusException responseException = (ResponseStatusException) exception;
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseException.getStatusCode().value());
        assertEquals("Category ID does not exist.", responseException.getReason());

        // Verificamos que el servicio fue llamado una vez
        verify(transactionService, times(1)).createTransaction(transaction);
    }

    @SuppressWarnings("null")
    @Test
    void testGetTransactionsByCategory_Success() throws Exception {
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());

        // Simulamos que el servicio liste las transacciones exitosamente
        when(transactionService.getTransactionsByCategory_id(anyLong())).thenReturn(transactions);

        // Act
        ResponseEntity<List<Transaction>> response = transactionController.getTransactionsByCategory_id(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        // Verificamos que se llamó al método createTransaction del servicio
        verify(transactionService, times(1)).getTransactionsByCategory_id(anyLong());
    }

    @Test
    void testGetTransactionsByCategory_NotFound() throws Exception {
        
        // Simulamos que el servicio devuelva una lista vaica
        when(transactionService.getTransactionsByCategory_id(anyLong())).thenReturn(List.of());

        //Verificamos que se lanza una ResponseStatusException al llamar al método
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.getTransactionsByCategory_id(1L);
        });


        // Verificamos que el código HTTP sea 404 (NOT_FOUND)
        ResponseStatusException responseException = (ResponseStatusException) exception;
        assertEquals(HttpStatus.NOT_FOUND.value(), responseException.getStatusCode().value());
        assertEquals("No transactions found for category", responseException.getReason());

        // Verificamos que se llamó al método createTransaction del servicio
        verify(transactionService, times(1)).getTransactionsByCategory_id(anyLong());
    }

    @SuppressWarnings("null")
    @Test
    void testGetTransactionsByStatus_Success() throws Exception {
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());

        // Simulamos que el servicio liste las transacciones exitosamente
        when(transactionService.getTransactionsByStatus(any(TransactionStatus.class))).thenReturn(transactions);

        // Act
        ResponseEntity<List<Transaction>> response = transactionController.getTransactionsByStatus(TransactionStatus.PENDIENTE);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        // Verificamos que se llamó al método createTransaction del servicio
        verify(transactionService, times(1)).getTransactionsByStatus(any(TransactionStatus.class));
    }

    @Test
    void testGetTransactionsByStatus_NotFound() throws Exception {
        
        // Simulamos que el servicio devuelva una lista vaica
        when(transactionService.getTransactionsByStatus(any(TransactionStatus.class))).thenReturn(List.of());

        //Verificamos que se lanza una ResponseStatusException al llamar al método
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.getTransactionsByStatus(TransactionStatus.APROBADA);
        });


        // Verificamos que el código HTTP sea 404 (NOT_FOUND)
        ResponseStatusException responseException = (ResponseStatusException) exception;
        assertEquals(HttpStatus.NOT_FOUND.value(), responseException.getStatusCode().value());
        assertEquals("No transactions found for status", responseException.getReason());

        // Verificamos que se llamó al método createTransaction del servicio
        verify(transactionService, times(1)).getTransactionsByStatus(any(TransactionStatus.class));

    }

    @SuppressWarnings("null")
    @Test
    void testGetTransactionsByDateRange_Success() throws Exception {
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());

        // Simulamos que el servicio liste las transacciones exitosamente
        when(transactionService.getTransactionsByDateRange(any(LocalDate.class), any(LocalDate.class))).thenReturn(transactions);

        //Creamos parametros de fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse("2025-02-18", formatter);
        LocalDate endDate = LocalDate.parse("2025-02-19", formatter);


        // Act
        ResponseEntity<List<Transaction>> response = transactionController.getTransactionsByDateRange(startDate,endDate);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        // Verificamos que se llamó al método createTransaction del servicio
        verify(transactionService, times(1)).getTransactionsByDateRange(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void testGetTransactionsByDateRange_NotFound() throws Exception {
        // Simulamos que el servicio devuelva una lista vaica
        when(transactionService.getTransactionsByDateRange(any(LocalDate.class), any(LocalDate.class))).thenReturn(List.of());

        //Creamos parametros de fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse("2025-02-18", formatter);
        LocalDate endDate = LocalDate.parse("2025-02-19", formatter);

        //Verificamos que se lanza una ResponseStatusException al llamar al método
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.getTransactionsByDateRange(startDate,endDate);
        });


        // Verificamos que el código HTTP sea 500 (INTERNAL_SERVER_ERROR)
        ResponseStatusException responseException = (ResponseStatusException) exception;
        assertEquals(HttpStatus.NOT_FOUND.value(), responseException.getStatusCode().value());
        assertEquals("No transactions found in date range", responseException.getReason());
        
        // Verificamos que se llamó al método createTransaction del servicio
        verify(transactionService, times(1)).getTransactionsByDateRange(any(LocalDate.class), any(LocalDate.class));

    }

    @SuppressWarnings("null")
    @Test
    void testUpdateTransactionStatus_Success() throws Exception {

        Transaction newTransaction = transaction;
        newTransaction.setStatus(TransactionStatus.APROBADA);

        // Simulamos que el servicio actualiza el estado de la transaccion exitosamente
        when(transactionService.updateTransactionStatus(anyLong(), any(TransactionStatus.class))).thenReturn(newTransaction);

        ResponseEntity<Transaction> response = transactionController.updateTransactionStatus(1L,TransactionStatus.APROBADA);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TransactionStatus.APROBADA, response.getBody().getStatus());

        // Verificamos que se llamó al método createTransaction del servicio
        verify(transactionService, times(1)).updateTransactionStatus(anyLong(), any(TransactionStatus.class));
    }

    @Test
    void testUpdateTransactionStatus_Failure() throws Exception {
        // Simulamos que el servicio falla actualizando el estado de la transaccion
        when(transactionService.updateTransactionStatus(anyLong(), any(TransactionStatus.class)))
                .thenThrow(new RuntimeException("Transaction update failed"));

        //Verificamos que se lanza una ResponseStatusException al llamar al método
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.updateTransactionStatus(1L,TransactionStatus.APROBADA);
        });

        // Verificamos que el código HTTP sea 500 (INTERNAL_SERVER_ERROR)
        ResponseStatusException responseException = (ResponseStatusException) exception;
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseException.getStatusCode().value());
        assertEquals("Error updating transaction status", responseException.getReason());

        // Verificamos que se llamó al método createTransaction del servicio
        verify(transactionService, times(1)).updateTransactionStatus(anyLong(), any(TransactionStatus.class));

    }

    @Test
    void testGetTotalExpensesByCategoryName_Success() throws Exception {
        Map<String, Double> summary = new HashMap<>();
        summary.put("Food", 100.0);
        summary.put("Transport", 50.0);
        
        // Simulamos que el servicio retorna un resumen de gastos
        when(transactionService.getTotalExpensesByCategoryName()).thenReturn(summary);

        ResponseEntity<Map<String, Double>> response = transactionController.getTotalExpensesByCategoryName();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(summary, response.getBody());

        // Verificamos que se llamó al método getTotalExpensesByCategoryName del servicio
        verify(transactionService, times(1)).getTotalExpensesByCategoryName();
    }

    @SuppressWarnings("null")
    @Test
    void testGetTotalExpensesByCategoryName_NoExpensesFound() throws Exception {

        // Simulamos que el servicio retorna un resumen de gastos vacio
        when(transactionService.getTotalExpensesByCategoryName()).thenReturn(new HashMap<>());

        //Verificamos que se lanza una ResponseStatusException al llamar al método
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.getTotalExpensesByCategoryName();
        });

        // Verificamos que el código HTTP sea 404 (NOT_FOUND)
        ResponseStatusException responseException = (ResponseStatusException) exception;
        assertEquals(HttpStatus.NOT_FOUND.value(), responseException.getStatusCode().value());
        assertEquals("No expenses found", responseException.getReason());

        // Verificamos que se llamó al método getTotalExpensesByCategoryName del servicio
        verify(transactionService, times(1)).getTotalExpensesByCategoryName();
    }

    @Test
    void testGetTotalExpensesByCategoryName_CategoryServiceFailure() throws Exception {

        // Simulamos que el servicio falla al generar el resumen
        when(transactionService.getTotalExpensesByCategoryName()).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving category name"));

        //Verificamos que se lanza una ResponseStatusException al llamar al método
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.getTotalExpensesByCategoryName();
        });

        // Verificamos que el código HTTP sea 500 (INTERNAL_SERVER_ERROR)
        ResponseStatusException responseException = (ResponseStatusException) exception;
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseException.getStatusCode().value());
        assertEquals("Error retrieving category name", responseException.getReason());

        // Verificamos que se llamó al método createTransaction del servicio
        verify(transactionService, times(1)).getTotalExpensesByCategoryName();

    }

}


