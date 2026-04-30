package org.example.yansiamsspring.controller;

import org.example.yansiamsspring.pojo.DailySupply;
import org.example.yansiamsspring.pojo.Result;
import org.example.yansiamsspring.service.DailySupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/daily-supplies")
public class DailySupplyController {

    @Autowired
    private DailySupplyService service;

    @GetMapping
    public Result<List<DailySupply>> list() {
        return Result.success(service.findAll());
    }

    @GetMapping("/{id}")
    public Result<DailySupply> detail(@PathVariable Long id) {
        DailySupply supply = service.findById(id);
        if (supply == null) return Result.error("记录不存在");
        return Result.success(supply);
    }

    @PostMapping
    public Result<?> create(@RequestBody DailySupply supply) {
        service.create(supply);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody DailySupply supply) {
        supply.setId(id);
        service.update(supply);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        service.softDelete(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<?> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        service.changeStatus(id, status);
        return Result.success();
    }
}
