package org.example.yansiamsspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginLog {
    private Long id;
    private Long userId;
    private String username;
    private String name;
    private String ip;
    private String userAgent;
    private LocalDateTime loginTime;
    private Integer status;
    private String failReason;
}
