package com.wip.assetmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.wip.assetmanagementsystem.entity.Category;

public class CategoryDTO {

    private Integer id;

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must be less than 100 characters")
    private String name;

    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

   
    public CategoryDTO() {}

    
    public CategoryDTO(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static CategoryDTO fromEntity(Category entity) {
        if (entity == null) return null;
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public Category toEntity() {
        Category entity = new Category();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setDescription(this.description);
        return entity;
    }
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
    