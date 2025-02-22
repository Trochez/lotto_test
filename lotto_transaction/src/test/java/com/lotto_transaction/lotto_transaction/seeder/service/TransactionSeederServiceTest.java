package com.lotto_transaction.lotto_transaction.seeder.service;


import com.lotto_transaction.lotto_transaction.entity.Transaction;
import com.lotto_transaction.lotto_transaction.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionSeederServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionSeederService transactionSeederService;

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

    @Test
    void testSeedTransactions_Success() {
        when(transactionRepository.saveAll(anyList())).thenReturn(mockTransactions);

        List<Transaction> result = transactionSeederService.seedTransactions();

        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals(new BigDecimal("50.0"), result.get(0).getAmount());
        assertEquals(new BigDecimal("60.0"), result.get(4).getAmount());

        verify(transactionRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testSeedTransactions_EmptyResult() {
        when(transactionRepository.saveAll(anyList())).thenReturn(List.of());

        List<Transaction> result = transactionSeederService.seedTransactions();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transactionRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testSeedTransactions_DatabaseFailure() {
        when(transactionRepository.saveAll(anyList())).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> transactionSeederService.seedTransactions());

        assertEquals("Database error", exception.getMessage());
        verify(transactionRepository, times(1)).saveAll(anyList());
    }
}
