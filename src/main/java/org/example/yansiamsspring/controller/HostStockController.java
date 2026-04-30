package org.example.yansiamsspring.controller;

import org.example.yansiamsspring.pojo.HostStock;
import org.example.yansiamsspring.pojo.Result;
import org.example.yansiamsspring.service.HostStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/host-stock")
public class HostStockController {

    @Autowired
    private HostStockService hostStockService;

    @GetMapping
    public Result<List<HostStock>> list(@RequestParam(required = false) String status) {
        return Result.success(hostStockService.findList(status));
    }

    @GetMapping("/in-stock")
    public Result<List<HostStock>> inStock() {
        return Result.success(hostStockService.findList("in_stock"));
    }

    @GetMapping("/{id}")
    public Result<HostStock> detail(@PathVariable Long id) {
        HostStock host = hostStockService.findById(id);
        if (host == null) return Result.error("记录不存在");
        return Result.success(host);
    }

    @PostMapping
    public Result<?> create(@RequestBody HostStock hostStock) {
        try {
            hostStockService.create(hostStock);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody HostStock hostStock) {
        hostStock.setId(id);
        try {
            hostStockService.update(hostStock);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        try {
            hostStockService.delete(id);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        hostStockService.updateStatus(id, status);
        return Result.success();
    }
}
