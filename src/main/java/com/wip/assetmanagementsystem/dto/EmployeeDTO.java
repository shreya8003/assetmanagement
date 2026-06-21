package com.wip.assetmanagementsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.wip.assetmanagementsystem.entity.Employee;

public class EmployeeDTO {

    private Integer id;

    @NotBlank(message = "Employee name is required")
    @Size(max = 100, message = "Employee name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;

    
    @NotBlank(message = "Company is required")
    @Size(max = 100, message = "Company must be less than 100 characters")
    private String company;

    public EmployeeDTO() {}

    public EmployeeDTO(Integer id, String name, String email, String company) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.company = company;
    }

    public static EmployeeDTO fromEntity(Employee entity) {
        if (entity == null) return null;
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setCompany(entity.getCompany());
        return dto;
    }

    public Employee toEntity() {
        Employee entity = new Employee();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setEmail(this.email);
        entity.setCompany(this.company);
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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}