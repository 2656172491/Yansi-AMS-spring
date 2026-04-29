package org.example.yansiamsspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.yansiamsspring.pojo.Asset;
import org.example.yansiamsspring.pojo.Result;
import org.example.yansiamsspring.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping
    public Result<List<Asset>> list() {
        return Result.success(assetService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Asset> detail(@PathVariable Long id) {
        Asset asset = assetService.findById(id);
        if (asset == null) {
            return Result.error("设备不存在");
        }
        return Result.success(asset);
    }

    @GetMapping("/departments")
    public Result<List<String>> departments() {
        return Result.success(assetService.findDepartments());
    }

    @GetMapping("/stock-pool")
    public Result<List<Asset>> stockPool(@RequestParam(required = false) String assetType) {
        return Result.success(assetService.findStockPool(assetType));
    }

    @PostMapping("/stock-in")
    public Result<?> stockIn(@RequestBody List<Asset> assets) {
        if (assets == null || assets.isEmpty()) {
            return Result.error("入库列表不能为空");
        }
        try {
            assetService.batchStockIn(assets);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/assign")
    public Result<?> assign(@RequestBody Map<String, Object> params) {
        Long assetId = Long.valueOf(params.get("assetId").toString());
        String department = (String) params.get("department");
        String keeper = (String) params.get("keeper");

        if (department == null || keeper == null) {
            return Result.error("部门和保管人不能为空");
        }

        try {
            assetService.assignAsset(assetId, department, keeper);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Asset asset) {
        asset.setId(id);
        assetService.updateAsset(asset);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        assetService.softDelete(id);
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<List<Map<String, Object>>> statistics() {
        return Result.success(assetService.getStatistics());
    }

    @GetMapping("/department-stats")
    public Result<List<Map<String, Object>>> departmentStats() {
        return Result.success(assetService.getDepartmentStats());
    }
}
