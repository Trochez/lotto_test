package com.lotto_category.lotto_category.seeder.controller;

import com.lotto_category.lotto_category.entity.Category;
import com.lotto_category.lotto_category.seeder.service.CategorySeederService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category-seeder")
public class CategorySeederController {

    @Autowired
    private CategorySeederService categorySeederService;


    @GetMapping("/seed")
    @Operation(summary = "Almacena datos de prueba", description = "Crea datos de prueba en la tabla de categorias")
    public ResponseEntity<List<Category>> seedCategories() {
        List<Category> seededCategories = categorySeederService.seedCategories();
        return ResponseEntity.ok(seededCategories);
    }
}