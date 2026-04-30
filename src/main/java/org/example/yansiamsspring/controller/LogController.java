package org.example.yansiamsspring.controller;

import org.example.yansiamsspring.pojo.LoginLog;
import org.example.yansiamsspring.pojo.OperationLog;
import org.example.yansiamsspring.pojo.Result;
import org.example.yansiamsspring.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/login")
    public Result<List<LoginLog>> loginLogs(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status) {
        return Result.success(logService.findLoginLogs(username, status));
    }

    @GetMapping("/operation")
    public Result<List<OperationLog>> operationLogs(
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String targetType) {
        return Result.success(logService.findOperationLogs(operationType, targetType));
    }

    @GetMapping("/flow-stats")
    public Result<List<Map<String, Object>>> flowStats(@RequestParam(defaultValue = "7") int days) {
        return Result.success(logService.getFlowStats(days));
    }
}
