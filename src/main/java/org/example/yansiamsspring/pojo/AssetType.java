package org.example.yansiamsspring.pojo;

import java.time.LocalDateTime;

public class AssetType {
    private Long id;
    private String name;
    private String code;
    private Integer sortOrder;
    private Integer status;
    private Integer snRequired;
    private Integer batchEnabled;
    private String prefix;
    private String listColumns;
    private String fieldsSchema;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getSnRequired() { return snRequired; }
    public void setSnRequired(Integer snRequired) { this.snRequired = snRequired; }
    public Integer getBatchEnabled() { return batchEnabled; }
    public void setBatchEnabled(Integer batchEnabled) { this.batchEnabled = batchEnabled; }
    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }
    public String getListColumns() { return listColumns; }
    public void setListColumns(String listColumns) { this.listColumns = listColumns; }
    public String getFieldsSchema() { return fieldsSchema; }
    public void setFieldsSchema(String fieldsSchema) { this.fieldsSchema = fieldsSchema; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
