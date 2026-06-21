package com.wip.assetmanagementsystem.controller;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wip.assetmanagementsystem.dto.CategoryDTO;
import com.wip.assetmanagementsystem.entity.Category;
import com.wip.assetmanagementsystem.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> saveCategory(
            @Valid @RequestBody CategoryDTO categoryDTO) {

        Category saved = categoryService.addCategory(categoryDTO.toEntity());
        return ResponseEntity.ok(CategoryDTO.fromEntity(saved));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> list = categoryService.getAllCategories().stream()
                .map(CategoryDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(
            @PathVariable Integer id) {

        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(CategoryDTO.fromEntity(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody CategoryDTO categoryDTO) {
        Category updated = categoryService.updateCategory(id, categoryDTO.toEntity());
        return ResponseEntity.ok(CategoryDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Integer id) {

        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category Deleted Successfully");
    }
}