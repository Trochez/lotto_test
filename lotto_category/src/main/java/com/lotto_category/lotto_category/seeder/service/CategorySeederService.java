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

        Category category1 = new Category();
        category1.setName("Food");

        Category category2 = new Category();
        category2.setName("Transport");

        Category category3 = new Category();
        category3.setName("Entertainment");

        Category category4 = new Category();
        category4.setName("Health");

        Category category5 = new Category();
        category5.setName("Education");

        List<Category> categories = Arrays.asList(
            category1,
            category2,
            category3,
            category4,
            category5
        );
        return categoryRepository.saveAll(categories);
    }
}