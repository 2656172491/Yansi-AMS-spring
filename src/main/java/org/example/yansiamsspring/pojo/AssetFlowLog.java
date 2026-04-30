package org.example.yansiamsspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetFlowLog {
    private Long id;
    private Long assetId;
    private String assetType;
    private String flowType;
    private Integer quantity;
    private String batchNo;
    private String operator;
    private Long operatorId;
    private LocalDateTime flowTime;
}
