package com.lotto_category.lotto_category.seeder.service;


import com.lotto_category.lotto_category.entity.Category;
import com.lotto_category.lotto_category.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategorySeederServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategorySeederService categorySeederService;

    private List<Category> mockCategories;

    @BeforeEach
    void setUp() {
        mockCategories = Arrays.asList(
                new Category(1L, "Food"),
                new Category(2L, "Transport"),
                new Category(3L, "Entertainment"),
                new Category(4L, "Health"),
                new Category(5L, "Education")
        );
    }

    @Test
    void testSeedCategories_Success() {
        when(categoryRepository.saveAll(anyList())).thenReturn(mockCategories);
        
        List<Category> result = categorySeederService.seedCategories();
        
        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals("Food", result.get(0).getName());
        assertEquals("Education", result.get(4).getName());
        
        verify(categoryRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testSeedCategories_EmptyResult() {
        when(categoryRepository.saveAll(anyList())).thenReturn(List.of());
        
        List<Category> result = categorySeederService.seedCategories();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(categoryRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testSeedCategories_DatabaseConnectionFailure() {
        when(categoryRepository.saveAll(anyList())).thenThrow(new DataIntegrityViolationException("Database connection error"));
        
        Exception exception = assertThrows(DataAccessException.class, () -> categorySeederService.seedCategories());
        
        assertEquals("Database connection error", exception.getMessage());
        verify(categoryRepository, times(1)).saveAll(anyList());
    }
}

