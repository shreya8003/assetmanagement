package com.wip.assetmanagementsystem.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import com.wip.assetmanagementsystem.entity.AssetAssignment;

public class AssetAssignmentDTO {

    private Integer id;

    @NotNull(message = "Asset is required")
    private AssetDTO asset;

    @NotNull(message = "Employee is required")
    private EmployeeDTO employee;

    @NotNull(message = "Assigned date is required")
    private LocalDate assignedDate;

    private LocalDate returnDate;

    private String status;

    public AssetAssignmentDTO() {}

    public AssetAssignmentDTO(Integer id, AssetDTO asset, EmployeeDTO employee,
                              LocalDate assignedDate, LocalDate returnDate, String status) {
        this.id = id;
        this.asset = asset;
        this.employee = employee;
        this.assignedDate = assignedDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public static AssetAssignmentDTO fromEntity(AssetAssignment entity) {
        if (entity == null) return null;
        AssetAssignmentDTO dto = new AssetAssignmentDTO();
        dto.setId(entity.getId());
        dto.setAssignedDate(entity.getAssignedDate());
        dto.setReturnDate(entity.getReturnDate());
        if (entity.getAsset() != null) dto.setAsset(AssetDTO.fromEntity(entity.getAsset()));
        if (entity.getEmployee() != null) dto.setEmployee(EmployeeDTO.fromEntity(entity.getEmployee()));
        return dto;
    }

    public AssetAssignment toEntity() {
        AssetAssignment entity = new AssetAssignment();
        entity.setId(this.id);
        entity.setAssignedDate(this.assignedDate);
        entity.setReturnDate(this.returnDate);
        if (this.asset != null) entity.setAsset(this.asset.toEntity());
        if (this.employee != null) entity.setEmployee(this.employee.toEntity());
        return entity;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public AssetDTO getAsset() { return asset; }
    public void setAsset(AssetDTO asset) { this.asset = asset; }

    public EmployeeDTO getEmployee() { return employee; }
    public void setEmployee(EmployeeDTO employee) { this.employee = employee; }

    public LocalDate getAssignedDate() { return assignedDate; }
    public void setAssignedDate(LocalDate assignedDate) { this.assignedDate = assignedDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
