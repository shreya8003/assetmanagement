package com.wip.assetmanagementsystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wip.assetmanagementsystem.entity.Category;
import com.wip.assetmanagementsystem.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category Not Found"));
    }

    @Override
    public Category updateCategory(Integer id, Category category) {

        Category existing = getCategoryById(id);

        existing.setName(category.getName());
        existing.setDescription(category.getDescription());

        return categoryRepository.save(existing);
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}