package com.lotto_category.lotto_category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.lotto_category.lotto_category.entity.Category;
import com.lotto_category.lotto_category.service.CategoryService;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categorías", description = "Operaciones relacionadas con las categorías")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista de todas las categorías disponibles.")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoria especifica", description = "Devuelve la categoria que corresponda con el id especificado")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        try{

            Category category = categoryService.getCategoryById(id);
            return new ResponseEntity<>(category, HttpStatus.OK);
            
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found", e);
        }
    }

    @PostMapping
    @Operation(summary = "Crear una nueva categoría", description = "Permite crear una nueva categoría de gastos.")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        try{

            Category createdCategory = categoryService.createCategory(category);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
            
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating caegory", e);
        }
    }
}
