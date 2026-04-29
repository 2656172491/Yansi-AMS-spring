package org.example.yansiamsspring.controller;

import org.example.yansiamsspring.pojo.PcAsset;
import org.example.yansiamsspring.pojo.Result;
import org.example.yansiamsspring.service.PcAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pc-assets")
public class PcAssetController {

    @Autowired
    private PcAssetService pcAssetService;

    @GetMapping
    public Result<List<PcAsset>> list(
            @RequestParam(required = false) String computerNo,
            @RequestParam(required = false) String hostSn,
            @RequestParam(required = false) String monitorSn,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String keeper) {
        return Result.success(pcAssetService.findList(computerNo, hostSn, monitorSn, department, keeper));
    }

    @GetMapping("/{id}")
    public Result<PcAsset> detail(@PathVariable Long id) {
        PcAsset pcAsset = pcAssetService.findById(id);
        if (pcAsset == null) return Result.error("记录不存在");
        return Result.success(pcAsset);
    }

    @GetMapping("/departments")
    public Result<List<String>> departments() {
        return Result.success(pcAssetService.findDepartments());
    }

    @PostMapping
    public Result<?> create(@RequestBody PcAsset pcAsset) {
        pcAssetService.create(pcAsset);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody PcAsset pcAsset) {
        pcAsset.setId(id);
        pcAssetService.update(pcAsset);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        pcAssetService.softDelete(id);
        return Result.success();
    }
}
