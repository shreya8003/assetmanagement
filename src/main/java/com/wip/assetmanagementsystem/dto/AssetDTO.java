package com.wip.assetmanagementsystem.dto;

import java.math.BigDecimal;
import java.sql.Date;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.wip.assetmanagementsystem.entity.Asset;

public class AssetDTO {

    private Integer id;

    @NotBlank(message = "Asset name is required")
    @Size(max = 100, message = "Asset name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Serial number is required")
    @Size(max = 50, message = "Serial number must be less than 50 characters")
    private String serialno;

    @Size(max = 50, message = "Price must be less than 50 characters")
    private String price;

    private Date purchaseDate;

    @DecimalMin(value = "0.0", inclusive = true, message = "Purchase price must be non-negative")
    private BigDecimal purchasePrice;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Category is required")
    private CategoryDTO category;

    public AssetDTO() {}

    public AssetDTO(Integer id, String name, String serialno,
                    String price, Date purchaseDate,
                    BigDecimal purchasePrice, String status,
                    CategoryDTO category) {
        this.id = id;
        this.name = name;
        this.serialno = serialno;
        this.price = price;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.status = status;
        this.category = category;
    }

    public static AssetDTO fromEntity(Asset entity) {
        if (entity == null) return null;
        AssetDTO dto = new AssetDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSerialno(entity.getSerialno());
        dto.setPrice(entity.getPrice());
        dto.setPurchaseDate(entity.getPurchaseDate());
        dto.setPurchasePrice(entity.getPurchasePrice());
        dto.setStatus(entity.getStatus());
        if (entity.getCategory() != null) {
            dto.setCategory(CategoryDTO.fromEntity(entity.getCategory()));
        }
        return dto;
    }

    public Asset toEntity() {
        Asset entity = new Asset();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setSerialno(this.serialno);
        entity.setPrice(this.price);
        entity.setPurchaseDate(this.purchaseDate);
        entity.setPurchasePrice(this.purchasePrice);
        entity.setStatus(this.status);
        if (this.category != null) {
            entity.setCategory(this.category.toEntity());
        }
        return entity;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSerialno() { return serialno; }
    public void setSerialno(String serialno) { this.serialno = serialno; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public CategoryDTO getCategory() { return category; }
    public void setCategory(CategoryDTO category) { this.category = category; }
}
