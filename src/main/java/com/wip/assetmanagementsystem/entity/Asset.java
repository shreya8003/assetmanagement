package com.wip.assetmanagementsystem.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name="asset")
public class Asset {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String serialno;
	private String price;
	private Date purchaseDate;
	private BigDecimal purchasePrice;
	private String status;
	
	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;
	
	@JsonIgnore
	@OneToMany(mappedBy="asset")
	private List<AssetAssignment> assignments;
	
	@JsonIgnore
	@OneToMany(mappedBy="asset")
	private List<MaintainanceLogs> maintainanceLogs;

	public Asset() {
		super();
	}

	public Asset(Integer id, String name, String serialno, String price, Date purchaseDate, BigDecimal purchasePrice,
			String status) {
		super();
		this.id = id;
		this.name = name;
		this.serialno = serialno;
		this.price = price;
		this.purchaseDate = purchaseDate;
		this.purchasePrice = purchasePrice;
		this.status = status;
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

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<AssetAssignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<AssetAssignment> assignments) {
		this.assignments = assignments;
	}

	public List<MaintainanceLogs> getMaintainanceLogs() {
		return maintainanceLogs;
	}

	public void setMaintainanceLogs(List<MaintainanceLogs> maintainanceLogs) {
		this.maintainanceLogs = maintainanceLogs;
	}
	
	
}
