package org.example.yansiamsspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LendingRecord {
    private Long id;
    private Long assetId;           // 借出设备ID
    private String borrower;        // 借用人
    private String borrowerDept;    // 借用部门
    private LocalDateTime lendTime; // 借出时间
    private LocalDate expectedReturn; // 预计归还
    private LocalDateTime actualReturn; // 实际归还
    private String handler;         // 经办人
    private Long handlerId;         // 经办人用户ID
    private String status;          // lent/returned
    private String remark;          // 备注
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
