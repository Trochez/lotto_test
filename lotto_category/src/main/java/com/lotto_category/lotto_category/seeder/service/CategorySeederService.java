package com.lotto_category.lotto_category.seeder.service;
import com.lotto_category.lotto_category.entity.Category;
import com.lotto_category.lotto_category.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CategorySeederService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> seedCategories() {
        List<Category> categories = Arrays.asList(
                new Category(1L, "Food"),
                new Category(2L, "Transport"),
                new Category(3L, "Entertainment"),
                new Category(4L, "Health"),
                new Category(5L, "Education")
        );
        return categoryRepository.saveAll(categories);
    }
}