package com.lotto_transaction.lotto_transaction.seeder.controller;


import com.lotto_transaction.lotto_transaction.entity.Transaction;
import com.lotto_transaction.lotto_transaction.seeder.service.TransactionSeederService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionSeederControllerTest {

    @Mock
    private TransactionSeederService transactionSeederService;

    @InjectMocks
    private TransactionSeederController transactionSeederController;

    private List<Transaction> mockTransactions;

    @BeforeEach
    void setUp() {
        Transaction t1 = new Transaction();
        t1.setCategoryId(1L);
        t1.setAmount(new BigDecimal("50.0"));

        Transaction t2 = new Transaction();
        t2.setCategoryId(2L);
        t2.setAmount(new BigDecimal("30.0"));

        Transaction t3 = new Transaction();
        t3.setCategoryId(3L);
        t3.setAmount(new BigDecimal("20.0"));

        Transaction t4 = new Transaction();
        t4.setCategoryId(3L);
        t4.setAmount(new BigDecimal("40.0"));

        Transaction t5 = new Transaction();
        t5.setCategoryId(4L);
        t5.setAmount(new BigDecimal("60.0"));

        mockTransactions = Arrays.asList(t1, t2, t3, t4, t5);
    }

    @SuppressWarnings("null")
    @Test
    void testSeedTransactions_Success() {
        when(transactionSeederService.seedTransactions()).thenReturn(mockTransactions);

        ResponseEntity<List<Transaction>> response = transactionSeederController.seedTransactions();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().size());
        assertEquals(new BigDecimal("50.0"), response.getBody().get(0).getAmount());
        assertEquals(new BigDecimal("60.0"), response.getBody().get(4).getAmount());
    }

    @SuppressWarnings("null")
    @Test
    void testSeedTransactions_EmptyResult() {
        when(transactionSeederService.seedTransactions()).thenReturn(List.of());

        ResponseEntity<List<Transaction>> response = transactionSeederController.seedTransactions();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testSeedTransactions_InternalServerError() {
        when(transactionSeederService.seedTransactions()).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> transactionSeederController.seedTransactions());

        assertEquals("Database error", exception.getMessage());
    }
}
