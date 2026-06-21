package com.wip.assetmanagementsystem.service;

import java.util.List;

import com.wip.assetmanagementsystem.entity.Category;

public interface CategoryService {
	Category addCategory(Category category);
	List<Category> getAllCategories();
	Category getCategoryById(Integer id);
	Category updateCategory(Integer id, Category category);
	void deleteCategory(Integer id);
}
