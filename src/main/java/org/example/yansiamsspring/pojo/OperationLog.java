package org.example.yansiamsspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationLog {
    private Long id;
    private Long userId;
    private String username;
    private String name;
    private String operationType;
    private String targetType;
    private Long targetId;
    private String detail;
    private String detailText;
    private Integer result;
    private String ip;
    private LocalDateTime operationTime;
}
