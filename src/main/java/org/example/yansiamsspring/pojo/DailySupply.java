package org.example.yansiamsspring.pojo;

import java.time.LocalDateTime;

public class DailySupply {
    private Long id;
    private String supplyType;
    private Integer quantity;
    private Integer warningQuantity;
    private Integer status;
    private Integer deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSupplyType() { return supplyType; }
    public void setSupplyType(String supplyType) { this.supplyType = supplyType; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getWarningQuantity() { return warningQuantity; }
    public void setWarningQuantity(Integer warningQuantity) { this.warningQuantity = warningQuantity; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
