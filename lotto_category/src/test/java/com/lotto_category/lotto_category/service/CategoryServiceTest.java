package com.lotto_category.lotto_category.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lotto_category.lotto_category.entity.Category;
import com.lotto_category.lotto_category.repository.CategoryRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Habilita Mockito en las pruebas
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository; // Simulamos el repositorio

    @InjectMocks
    private CategoryService categoryService; // Inyectamos el servicio a probar

    @Test
    public void testCreateCategory_Success() {
        // Arrange
        Category category = new Category();
        category.setName("Alimentación");

        // Simulamos que no existe una categoría con el mismo nombre
        when(categoryRepository.existsByName("Alimentación")).thenReturn(false);

        // Simulamos que se guarda la categoría correctamente
        when(categoryRepository.save(category)).thenReturn(category);

        // Act
        Category createdCategory = categoryService.createCategory(category);

        // Assert
        assertNotNull(createdCategory);
        assertEquals("Alimentación", createdCategory.getName());

        // Verificamos que se llamó al método existsByName
        verify(categoryRepository, times(1)).existsByName("Alimentación");

        // Verificamos que se llamó al método save
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void testCreateCategory_DuplicateCategory() {
        // Arrange
        Category category = new Category();
        category.setName("Alimentación");

        // Simulamos que ya existe una categoría con el mismo nombre
        when(categoryRepository.existsByName("Alimentación")).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            categoryService.createCategory(category);
        });

        // Verificamos el mensaje de la excepción
        assertEquals("Category already exists", exception.getMessage());

        // Verificamos que se llamó al método existsByName
        verify(categoryRepository, times(1)).existsByName("Alimentación");

        // Verificamos que NO se llamó al método save
        verify(categoryRepository, never()).save(category);
    }

    @Test
    void testGetCategoryById_Success() {

        Category category = new Category();
        category.setName("Test Category");

        // Simulamos que se obtiene la categoria a buscar
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act
        Category result = categoryService.getCategoryById(1L);
        
        //Assert
        assertNotNull(result);
        assertEquals("Test Category", result.getName());
    }

    @Test
    void testGetCategoryById_NotFound() {

        // Simulamos que no se encuentra la categoria a buscar
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            categoryService.getCategoryById(1L);
        });

        // Verificamos el mensaje de la excepción
        assertEquals("Category not found", exception.getMessage());

        // Verificamos que se llamó al método existsByName
        verify(categoryRepository, times(1)).findById(1L);

    }
}