package org.example.yansiamsspring.controller;

import org.example.yansiamsspring.pojo.MonitorStock;
import org.example.yansiamsspring.pojo.Result;
import org.example.yansiamsspring.service.MonitorStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitor-stock")
public class MonitorStockController {

    @Autowired
    private MonitorStockService monitorStockService;

    @GetMapping
    public Result<List<MonitorStock>> list(@RequestParam(required = false) String status) {
        return Result.success(monitorStockService.findList(status));
    }

    @GetMapping("/in-stock")
    public Result<List<MonitorStock>> inStock() {
        return Result.success(monitorStockService.findList("in_stock"));
    }

    @GetMapping("/{id}")
    public Result<MonitorStock> detail(@PathVariable Long id) {
        MonitorStock monitor = monitorStockService.findById(id);
        if (monitor == null) return Result.error("记录不存在");
        return Result.success(monitor);
    }

    @PostMapping
    public Result<?> create(@RequestBody MonitorStock monitorStock) {
        try {
            monitorStockService.create(monitorStock);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody MonitorStock monitorStock) {
        monitorStock.setId(id);
        try {
            monitorStockService.update(monitorStock);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        try {
            monitorStockService.delete(id);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        monitorStockService.updateStatus(id, status);
        return Result.success();
    }
}
