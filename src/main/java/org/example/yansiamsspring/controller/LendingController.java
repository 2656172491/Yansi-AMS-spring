package org.example.yansiamsspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.yansiamsspring.pojo.LendingRecord;
import org.example.yansiamsspring.pojo.Result;
import org.example.yansiamsspring.service.LendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lending")
public class LendingController {

    @Autowired
    private LendingService lendingService;

    @GetMapping
    public Result<List<LendingRecord>> list(@RequestParam(required = false) String status) {
        return Result.success(lendingService.findList(status));
    }

    @GetMapping("/{id}")
    public Result<LendingRecord> detail(@PathVariable Long id) {
        LendingRecord record = lendingService.findById(id);
        if (record == null) {
            return Result.error("记录不存在");
        }
        return Result.success(record);
    }

    @GetMapping("/overdue")
    public Result<List<LendingRecord>> overdue() {
        return Result.success(lendingService.findOverdue());
    }

    @PostMapping
    public Result<?> lend(HttpServletRequest request, @RequestBody LendingRecord record) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role) && !"teacher".equals(role)) {
            return Result.error("无权限");
        }

        Long userId = (Long) request.getAttribute("userId");
        String name = (String) request.getAttribute("name");
        record.setHandlerId(userId);
        record.setHandler(name);

        try {
            lendingService.lend(record);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/return")
    public Result<?> returnDevice(HttpServletRequest request, @PathVariable Long id) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role) && !"teacher".equals(role)) {
            return Result.error("无权限");
        }

        try {
            lendingService.returnDevice(id);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/stats")
    public Result<List<Map<String, Object>>> stats() {
        return Result.success(lendingService.getLendingStats());
    }
}
