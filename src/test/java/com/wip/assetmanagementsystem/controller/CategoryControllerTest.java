package com.wip.assetmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.assetmanagementsystem.entity.Category;
import com.wip.assetmanagementsystem.security.CustomUserDetailsService;
import com.wip.assetmanagementsystem.security.JwtUtil;
import com.wip.assetmanagementsystem.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "admin")
@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

	/*
	 * @Test void testSaveCategory() throws Exception { Category category = new
	 * Category(); category.setId(1); category.setName("Electronics");
	 * 
	 * when(categoryService.saveCategory(any(Category.class)))
	 * .thenReturn(category);
	 * 
	 * mockMvc.perform(post("/api/categories") .with(csrf())
	 * .contentType(MediaType.APPLICATION_JSON)
	 * .content(objectMapper.writeValueAsString(category)))
	 * .andExpect(status().isCreated()); }
	 */
    @Test
    void testGetAllCategories() throws Exception {
        Category c1 = new Category();
        c1.setName("Electronics");

        when(categoryService.getAllCategories())
            .thenReturn(Arrays.asList(c1));

        mockMvc.perform(get("/api/categories"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetCategoryById() throws Exception {
        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        when(categoryService.getCategoryById(1))
            .thenReturn(category);

        mockMvc.perform(get("/api/categories/1"))
            .andExpect(status().isOk());
    }

    @Test
    void testUpdateCategory() throws Exception {
        Category updated = new Category();
        updated.setId(1);
        updated.setName("Updated");

        when(categoryService.updateCategory(eq(1), any(Category.class)))
            .thenReturn(updated);

        mockMvc.perform(put("/api/categories/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isOk());
    }

    @Test
    void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(1);

        mockMvc.perform(delete("/api/categories/1")
                .with(csrf()))
            .andExpect(status().isOk());
    }
}