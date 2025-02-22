package com.lotto_category.lotto_category.seeder.controller;


import com.lotto_category.lotto_category.entity.Category;
import com.lotto_category.lotto_category.seeder.service.CategorySeederService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategorySeederControllerTest {
    @Mock
    private CategorySeederService categorySeederService;

    @InjectMocks
    private CategorySeederController categorySeederController;

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

    @SuppressWarnings("null")
    @Test
    void testSeedCategories_Success() {
        when(categorySeederService.seedCategories()).thenReturn(mockCategories);
        
        ResponseEntity<List<Category>> response = categorySeederController.seedCategories();
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().size());
        assertEquals("Food", response.getBody().get(0).getName());
        assertEquals("Education", response.getBody().get(4).getName());
    }

    @SuppressWarnings("null")
    @Test
    void testSeedCategories_EmptyResponse() {
        when(categorySeederService.seedCategories()).thenReturn(List.of());
        
        ResponseEntity<List<Category>> response = categorySeederController.seedCategories();
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testSeedCategories_InternalServerError() {
        when(categorySeederService.seedCategories()).thenThrow(new RuntimeException("Database error"));
        
        Exception exception = assertThrows(RuntimeException.class, () -> categorySeederController.seedCategories());
        
        assertEquals("Database error", exception.getMessage());
    }
}
