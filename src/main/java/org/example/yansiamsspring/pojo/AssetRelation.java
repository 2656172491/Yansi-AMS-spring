package org.example.yansiamsspring.pojo;

import java.time.LocalDateTime;

public class AssetRelation {
    private Long id;
    private Long hostAssetId;
    private Long relatedAssetId;
    private String relationType;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getHostAssetId() { return hostAssetId; }
    public void setHostAssetId(Long hostAssetId) { this.hostAssetId = hostAssetId; }
    public Long getRelatedAssetId() { return relatedAssetId; }
    public void setRelatedAssetId(Long relatedAssetId) { this.relatedAssetId = relatedAssetId; }
    public String getRelationType() { return relationType; }
    public void setRelationType(String relationType) { this.relationType = relationType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
