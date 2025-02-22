package com.lotto_transaction.lotto_transaction.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.lotto_transaction.lotto_transaction.client.dto.CategoryDTO;

import org.springframework.http.ResponseEntity;

@FeignClient(name = "lotto-category") // Usa el nombre registrado en Eureka
public interface CategoryClient {
    
    @GetMapping("/categories/{id}") // Ruta en el servicio de categorías
    ResponseEntity<Void> validateCategoryExists(@PathVariable("id") Long id);

    @GetMapping("/categories/{id}") // Ruta en el servicio de categorías
    ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("id") Long id);
}