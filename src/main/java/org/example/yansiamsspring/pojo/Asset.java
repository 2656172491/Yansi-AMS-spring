package org.example.yansiamsspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asset {
    private Long id;
    private String computerNo;      // 电脑编号
    private String macAddress;      // MAC地址
    private String department;      // 保管部门
    private String keeper;          // 保管人
    private String monitorSn;       // 显示器序列号
    private String hostSn;          // 主机序列号
    private String remark;          // 备注
    private Integer status;         // 1=正常运行, 0=已停用
    private String assetType;       // 设备类型: desktop/monitor/lock/camera/card/laptop/pointer/network/key/unknown
    private String stockStatus;     // 库存状态: in_stock/in_use/scrapped/returned
    private LocalDateTime purchaseTime;   // 采购入库时间
    private String purchaseBatch;         // 采购批次号
    private LocalDateTime lastInspectionTime;  // 最近巡检时间
    private String lastInspectionUser;         // 最近巡检人
    private String lastInspectionBatch;        // 最近巡检批次号
    private Integer deleted;        // 软删除: 0=正常, 1=已删除
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
