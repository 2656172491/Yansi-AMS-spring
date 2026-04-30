package org.example.yansiamsspring.pojo;

import java.time.LocalDateTime;

public class PcAsset {
    private Long id;
    private String computerNo;
    private Long hostId;
    private Long monitorId;
    private String macAddress;
    private String department;
    private String keeper;
    private String remark;
    private Integer status;
    private LocalDateTime lastInspectionTime;
    private String lastInspectionUser;
    private Integer deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // JOIN fields (非数据库字段，查询时从 host_stock/monitor_stock 获取)
    private String hostSn;
    private String monitorSn;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getComputerNo() { return computerNo; }
    public void setComputerNo(String computerNo) { this.computerNo = computerNo; }
    public Long getHostId() { return hostId; }
    public void setHostId(Long hostId) { this.hostId = hostId; }
    public Long getMonitorId() { return monitorId; }
    public void setMonitorId(Long monitorId) { this.monitorId = monitorId; }
    public String getMacAddress() { return macAddress; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getKeeper() { return keeper; }
    public void setKeeper(String keeper) { this.keeper = keeper; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getLastInspectionTime() { return lastInspectionTime; }
    public void setLastInspectionTime(LocalDateTime lastInspectionTime) { this.lastInspectionTime = lastInspectionTime; }
    public String getLastInspectionUser() { return lastInspectionUser; }
    public void setLastInspectionUser(String lastInspectionUser) { this.lastInspectionUser = lastInspectionUser; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getHostSn() { return hostSn; }
    public void setHostSn(String hostSn) { this.hostSn = hostSn; }
    public String getMonitorSn() { return monitorSn; }
    public void setMonitorSn(String monitorSn) { this.monitorSn = monitorSn; }
}
