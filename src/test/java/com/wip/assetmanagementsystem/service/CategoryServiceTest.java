package com.wip.assetmanagementsystem.service;

import com.wip.assetmanagementsystem.entity.Category;
import com.wip.assetmanagementsystem.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCategory() {
        Category category = new Category();
        category.setName("Electronics");
        category.setDescription("Electronic items");

        when(categoryRepository.save(category))
            .thenReturn(category);

        Category saved =
            categoryService.addCategory(category);

        assertNotNull(saved);
        assertEquals("Electronics", saved.getName());
    }

    @Test
    void testGetAllCategories() {
        Category c1 = new Category();
        c1.setName("Electronics");
        Category c2 = new Category();
        c2.setName("Furniture");

        when(categoryRepository.findAll())
            .thenReturn(Arrays.asList(c1, c2));

        List<Category> list =
            categoryService.getAllCategories();

        assertEquals(2, list.size());
    }

    @Test
    void testGetCategoryById() {
        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        when(categoryRepository.findById(1))
            .thenReturn(Optional.of(category));

        Category found =
            categoryService.getCategoryById(1);

        assertNotNull(found);
        assertEquals("Electronics", found.getName());
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryRepository)
            .deleteById(1);

        categoryService.deleteCategory(1);

        verify(categoryRepository, times(1))
            .deleteById(1);
    }
}