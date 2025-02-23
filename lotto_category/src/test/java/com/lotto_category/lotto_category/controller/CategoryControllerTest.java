
package com.lotto_category.lotto_category.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.lotto_category.lotto_category.entity.Category;
import com.lotto_category.lotto_category.service.CategoryService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita Mockito en las pruebas
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService; // Simulamos el servicio

    @InjectMocks
    private CategoryController categoryController; // Inyectamos el controlador a probar

    @SuppressWarnings("null")
    @Test
    public void testCreateCategory_Success() throws Exception {

        Category category = new Category();
        category.setName("Alimentación");

        // Simulamos que el servicio crea la categoría exitosamente
        when(categoryService.createCategory(category)).thenReturn(category);

        // Act
        ResponseEntity<Category> response = categoryController.createCategory(category);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Alimentación", response.getBody().getName());

        // Verificamos que se llamó al método createCategory del servicio
        verify(categoryService, times(1)).createCategory(category);
    }

    @Test
    public void testCreateCategory_Failure() throws Exception {

        // Simulamos que el servicio crea la categoría exitosamente
        when(categoryService.createCategory(null)).thenThrow(new RuntimeException("Category creation failed"));

        //Verificamos que se lanza una ResponseStatusException al llamar al método
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            categoryController.createCategory(null);
        });

        // Verificamos que el código HTTP sea 500 (INTERNAL_SERVER_ERROR)
        ResponseStatusException responseException = (ResponseStatusException) exception;
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseException.getStatusCode().value());
        assertEquals("Error creating caegory", responseException.getReason());

        // Verificamos que se llamó al método createCategory del servicio
        verify(categoryService, times(1)).createCategory(null);
    }

    @Test
    public void testCreateCategory_DuplicateCategory() throws Exception {

        Category category = new Category();
        category.setName("Alimentación");

        // Simulamos que el servicio lanza una excepción por categoría duplicada
        when(categoryService.createCategory(category)).thenThrow(new RuntimeException("Category already exists"));

        //Verificamos que se lanza una ResponseStatusException al llamar al método
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            categoryController.createCategory(category);
        });

        // Verificamos que el código HTTP sea 500 (INTERNAL_SERVER_ERROR)
        ResponseStatusException responseException = (ResponseStatusException) exception;
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), responseException.getStatusCode().value());
        assertEquals("Category already exists", responseException.getReason());
        
        // Verificamos que se llamó al método createCategory del servicio
        verify(categoryService, times(1)).createCategory(category);
    }

    @SuppressWarnings("null")
    @Test
    void testGetCategoryById_Success() throws Exception {

        Category category = new Category();
        category.setName("Alimentación");

        // Simulamos que el servicio retorna una caegoria
        when(categoryService.getCategoryById(1L)).thenReturn(category);

        // Act
        ResponseEntity<Category> response = categoryController.getCategoryById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Alimentación", response.getBody().getName());

        // Verificamos que se llamó al método createCategory del servicio
        verify(categoryService, times(1)).getCategoryById(1L);
        
    }

    @Test
    void testGetCategoryById_NotFound() throws Exception {

        // Simulamos que el servicio no encuentra ninguna caegoria
        when(categoryService.getCategoryById(1L)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Category not found"));

        //Verificamos que se lanza una ResponseStatusException al llamar al método
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            categoryController.getCategoryById(1L);
        });

        // Verificamos que el código HTTP sea 404 (NOT_FOUND)
        ResponseStatusException responseException = (ResponseStatusException) exception;
        assertEquals(HttpStatus.NOT_FOUND.value(), responseException.getStatusCode().value());
        assertEquals("Category not found", responseException.getReason());
        
        // Verificamos que se llamó al método getCategoryById del servicio
        verify(categoryService, times(1)).getCategoryById(1L);

    }
}