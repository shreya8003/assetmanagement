package com.wip.assetmanagementsystem.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name="employee")
public class Employee {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name;
	@Column(unique=true)
	private String email;
	private String company;

	@Column(unique=true)
	@JsonIgnore
	private String username;

	@JsonIgnore
	private String password;

	@JsonIgnore
	private String role;

	@JsonIgnore
	@OneToMany(mappedBy="employee")
	private List<AssetAssignment> assignments;

	public Employee() {
		super();
	}

	public Employee(Integer id, String name, String email, String company) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.company = company;
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

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }

	public List<AssetAssignment> getAssignments() { return assignments; }
	public void setAssignments(List<AssetAssignment> assignments) { this.assignments = assignments; }
}
