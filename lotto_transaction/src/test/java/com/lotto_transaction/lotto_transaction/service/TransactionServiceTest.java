package com.lotto_transaction.lotto_transaction.service;

import com.lotto_transaction.lotto_transaction.client.CategoryClient;
import com.lotto_transaction.lotto_transaction.client.dto.CategoryDTO;
import com.lotto_transaction.lotto_transaction.entity.Transaction;
import com.lotto_transaction.lotto_transaction.enumValues.TransactionStatus;
import com.lotto_transaction.lotto_transaction.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryClient categoryClient;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;
    private Transaction transactionAux;

    private CategoryDTO category1;
    private CategoryDTO category2;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal(100.1));
        transaction.setStatus(TransactionStatus.PENDIENTE);
        transaction.setDate(LocalDate.now());
        transaction.setCategoryId(1L);

        transactionAux = new Transaction();
        transactionAux.setId(2L);
        transactionAux.setAmount(new BigDecimal(25.16));
        transactionAux.setStatus(TransactionStatus.PENDIENTE);
        transactionAux.setDate(LocalDate.now());
        transactionAux.setCategoryId(200L);

        category1 = new CategoryDTO(1L, "Food");
        category2 = new CategoryDTO(200L, "Transport");
    }

    @Test
    void testGetAllTransactions() {
        List<Transaction> transactions = Arrays.asList(transaction, transaction);

        // Simulamos que el repositorio crea la transaccion exitosamente
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Act
        List<Transaction> result = transactionService.getAllTransactions();

        // Assert
        assertEquals(2, result.size());

        // Verificamos que se llamó al método findAll del repositorio
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testCreateTransaction_Success() {

        // Simulamos que el repositorio crea la transaccion exitosamente
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        Transaction createdTransaction = transactionService.createTransaction(transaction);

        // Assert
        assertNotNull(createdTransaction);
        assertEquals(new BigDecimal(100.1), createdTransaction.getAmount());

        // Verificamos que se llamó al método save del repositorio
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testCreateTransaction_CategoryNotFound() {

        //Simulamos que no se encuentra la categoria
        doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Category not found"))
                .when(categoryClient).validateCategoryExists(transaction.getCategoryId());
        
        //Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
                transactionService.createTransaction(transaction));
        
        //Assert
        assertEquals("400 BAD_REQUEST \"Category ID does not exist.\"", exception.getMessage());

        // Verificamos que se llamó al método validateCategoryExists del cliente
        verify(categoryClient, times(1)).validateCategoryExists(transaction.getCategoryId());
    }

    @Test
    void testGetTransactionsByStatus() {
        List<Transaction> transactions = Arrays.asList(transaction);

        // Simulamos que el repositorio consulta las transacciones exitosamente
        when(transactionRepository.findByStatus(TransactionStatus.PENDIENTE)).thenReturn(transactions);

        // Act
        List<Transaction> result = transactionService.getTransactionsByStatus(TransactionStatus.PENDIENTE);

        // Assert
        assertEquals(1, result.size());

        // Verificamos que se llamó al método findByStatus del repositorio
        verify(transactionRepository, times(1)).findByStatus(TransactionStatus.PENDIENTE);
    }

    @Test
    void testGetTransactionsByCategory_id() {
        List<Transaction> transactions = Arrays.asList(transaction);

        // Simulamos que el repositorio consulta las transacciones exitosamente
        when(transactionRepository.findByCategoryId(1L)).thenReturn(transactions);

        // Act
        List<Transaction> result = transactionService.getTransactionsByCategory_id(1L);

        // Assert
        assertEquals(1, result.size());

        // Verificamos que se llamó al método findByStatus del repositorio
        verify(transactionRepository, times(1)).findByCategoryId(1L);
    }

    @Test
    void testGetTransactionsByDateRange() {
        List<Transaction> transactions = Arrays.asList(transaction);

        // Simulamos que el repositorio consulta las transacciones exitosamente
        when(transactionRepository.findByDateBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(transactions);

        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();

        // Act
        List<Transaction> result = transactionService.getTransactionsByDateRange(startDate,endDate);

        // Assert
        assertEquals(1, result.size());

        // Verificamos que se llamó al método getTransactionsByDateRange del repositorio
        verify(transactionRepository, times(1)).findByDateBetween(startDate,endDate);
    }

    @Test
    void testUpdateTransactionStatus_Success() {

        // Simulamos que el repositorio consulta las transacciones exitosamente
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        // Simulamos que el repositorio actualiza la transaccion exitosamente
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        Transaction updatedTransaction = transactionService.updateTransactionStatus(1L, TransactionStatus.APROBADA);

        // Assert
        assertEquals(TransactionStatus.APROBADA, updatedTransaction.getStatus());

        //Se actualiza estado de transaccion de prueba
        Transaction newTransaction = transaction;
        newTransaction.setStatus( TransactionStatus.APROBADA);

        // Verificamos que se llamó al método getTransactionsByDateRange del repositorio
        verify(transactionRepository, times(1)).save(newTransaction);
    }

    @Test
    void testUpdateTransactionStatus_NotFound() {
        // Simulamos que el repositorio actualiza la transaccion sin coincidencias
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(RuntimeException.class, () -> transactionService.updateTransactionStatus(1L, TransactionStatus.APROBADA));

        // Verificamos que se llamó al método findById del repositorio
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTotalExpensesByCategoryName_Success() {
        List<Transaction> transactions = Arrays.asList(transaction, transactionAux);

        // Simulamos que el repositorio retorna transacciones
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Simulamos que el cliente retorna una categoria ante el id 100
        when(categoryClient.getCategoryById(1L)).thenReturn(ResponseEntity.ok(category1));

        // Simulamos que el cliente retorna una categoria ante el id 200
        when(categoryClient.getCategoryById(200L)).thenReturn(ResponseEntity.ok(category2));

        // Act
        Map<String, Double> result = transactionService.getTotalExpensesByCategoryName();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(100.1, result.get("Food"));
        assertEquals(25.16, result.get("Transport"));

        // Verificamos que se llamó al método findById del repositorio
        verify(transactionRepository, times(1)).findAll();

        // Verificamos que se llamó al método findById del repositorio
        verify(categoryClient, times(2)).getCategoryById(anyLong());
    }

    @Test
    void testGetTotalExpensesByCategoryName_NoTransactions() {

        // Simulamos que el repositorio retorna una lista vacia
        when(transactionRepository.findAll()).thenReturn(List.of());

        //Act
        Map<String, Double> result = transactionService.getTotalExpensesByCategoryName();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verificamos que se llamó al método findAll del repositorio
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testGetTotalExpensesByCategoryName_CategoryServiceFailure() {
        List<Transaction> transactions = Arrays.asList(transaction, transaction);

        // Simulamos que el repositorio retorna una lista de transacciones
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Simulamos que el cliente no encuentra ninguna categoria ante el id 100
        when(categoryClient.getCategoryById(1L)).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving category name"));

        //Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> 
                transactionService.getTotalExpensesByCategoryName());
        
        //Assert
        assertEquals("500 INTERNAL_SERVER_ERROR \"Error retrieving category name\"", exception.getMessage());

        // Verificamos que se llamó al método getCategoryById del cliente
        verify(categoryClient, times(1)).getCategoryById(1L);

        // Verificamos que se llamó al método findAll del repositorio
        verify(transactionRepository, times(1)).findAll();

    }
}
