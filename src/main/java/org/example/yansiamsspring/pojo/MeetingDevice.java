package org.example.yansiamsspring.pojo;

import java.time.LocalDateTime;

public class MeetingDevice {
    private Long id;
    private String deviceNo;
    private String sn;
    private String remark;
    private Integer status;
    private Integer deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDeviceNo() { return deviceNo; }
    public void setDeviceNo(String deviceNo) { this.deviceNo = deviceNo; }
    public String getSn() { return sn; }
    public void setSn(String sn) { this.sn = sn; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
