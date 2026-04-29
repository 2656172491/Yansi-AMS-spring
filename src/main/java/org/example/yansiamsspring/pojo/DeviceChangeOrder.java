package org.example.yansiamsspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceChangeOrder {
    private Long id;
    private String orderNo;         // 单号 CHG-{timestamp}
    private String orderType;       // replace(更换) / recycle(回收)
    private Long assetId;           // 涉及设备ID
    private Long newAssetId;        // 新设备ID(更换时)
    private String reporter;        // 报修人
    private String reporterDept;    // 报修人部门
    private String faultDesc;       // 故障描述
    private String handler;         // 经办人
    private Long handlerId;         // 经办人用户ID
    private String status;          // pending -> checking -> done
    private String result;          // repaired/replaced/recycled
    private String remark;          // 备注
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
