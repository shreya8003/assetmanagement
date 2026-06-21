package com.wip.assetmanagementsystem.dto;

import java.sql.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.wip.assetmanagementsystem.entity.MaintainanceLogs;

public class MaintainanceLogsDTO {

    private Integer id;

    @NotNull(message ="Asset is required")
    private AssetDTO asset;

    private Date maintainanceDate;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    private String status;
    private String rejectionMessage;
    private String submittedByUsername;

    public MaintainanceLogsDTO() {}

    public static MaintainanceLogsDTO fromEntity(MaintainanceLogs entity) {
        if (entity == null) return null;
        MaintainanceLogsDTO dto = new MaintainanceLogsDTO();
        dto.setId(entity.getId());
        dto.setMaintainanceDate(entity.getMaintainanceDate());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setRejectionMessage(entity.getRejectionMessage());
        dto.setSubmittedByUsername(entity.getSubmittedByUsername());
        if (entity.getAsset() != null) {
            dto.setAsset(AssetDTO.fromEntity(entity.getAsset()));
        }
        return dto;
    }

    public MaintainanceLogs toEntity() {
        MaintainanceLogs entity = new MaintainanceLogs();
        entity.setId(this.id);
        entity.setMaintainanceDate(this.maintainanceDate);
        entity.setDescription(this.description);
        if (this.status != null) entity.setStatus(this.status);
        entity.setRejectionMessage(this.rejectionMessage);
        entity.setSubmittedByUsername(this.submittedByUsername);
        if (this.asset != null) {
            entity.setAsset(this.asset.toEntity());
        }
        return entity;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public AssetDTO getAsset() { return asset; }
    public void setAsset(AssetDTO asset) { this.asset = asset; }

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
}
