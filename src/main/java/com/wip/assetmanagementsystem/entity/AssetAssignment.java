package com.wip.assetmanagementsystem.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name="assetAssignment")
public class AssetAssignment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private LocalDate assignedDate;
	private LocalDate returnDate;
	
	@ManyToOne
	@JoinColumn(name="asset_id")
	private Asset asset;
	
	@ManyToOne
	@JoinColumn(name="employee_id")
	private Employee employee;

	public AssetAssignment() {
		super();
	}

	public AssetAssignment(Integer id, LocalDate assignedDate, LocalDate returnDate) {
		super();
		this.id = id;
		this.assignedDate = assignedDate;
		this.returnDate = returnDate;
	
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(LocalDate assignedDate) {
		this.assignedDate = assignedDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}