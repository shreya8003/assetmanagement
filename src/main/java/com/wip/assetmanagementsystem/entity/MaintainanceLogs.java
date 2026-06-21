package com.wip.assetmanagementsystem.entity;

import java.sql.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="maintainanceLogs")
public class MaintainanceLogs {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private Date maintainanceDate;
	@NotBlank(message="Description is required")
	private String description;
	private String status = "Pending";
	@Column(length = 1000)
	private String rejectionMessage;
	private String submittedByUsername;

	@ManyToOne
	@JoinColumn(name="asset_id")
	private Asset asset;

	public MaintainanceLogs() { super(); }

	public MaintainanceLogs(Integer id, Date maintainanceDate, String description) {
		super();
		this.id = id;
		this.maintainanceDate = maintainanceDate;
		this.description = description;
	}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }

	public Date getMaintainanceDate() { return maintainanceDate; }
	public void setMaintainanceDate(Date maintainanceDate) { this.maintainanceDate = maintainanceDate; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

	public String getRejectionMessage() { return rejectionMessage; }
	public void setRejectionMessage(String rejectionMessage) { this.rejectionMessage = rejectionMessage; }

	public String getSubmittedByUsername() { return submittedByUsername; }
	public void setSubmittedByUsername(String submittedByUsername) { this.submittedByUsername = submittedByUsername; }

	public Asset getAsset() { return asset; }
	public void setAsset(Asset asset) { this.asset = asset; }
}
