package org.example.yansiamsspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.yansiamsspring.pojo.*;
import org.example.yansiamsspring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private AssetBatchService assetBatchService;

    @Autowired
    private AssetRelationService assetRelationService;

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
    public Result<?> stockIn(@RequestBody List<Asset> assets, HttpServletRequest request) {
        if (assets == null || assets.isEmpty()) {
            return Result.error("入库列表不能为空");
        }
        try {
            String name = (String) request.getAttribute("name");
            Long userId = (Long) request.getAttribute("userId");
            assetService.batchStockIn(assets, name, userId);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/assign")
    public Result<?> assign(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long assetId = Long.valueOf(params.get("assetId").toString());
        String department = (String) params.get("department");
        String keeper = (String) params.get("keeper");

        if (department == null || keeper == null) {
            return Result.error("部门和保管人不能为空");
        }

        try {
            String name = (String) request.getAttribute("name");
            Long userId = (Long) request.getAttribute("userId");
            assetService.assignAsset(assetId, department, keeper, name, userId);
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

    @GetMapping("/stock-batch")
    public Result<List<Map<String, Object>>> stockBatch() {
        return Result.success(assetService.getStockBatch());
    }

    @GetMapping("/stock-batch-items")
    public Result<List<Asset>> stockBatchItems(@RequestParam String assetType, @RequestParam String batch) {
        return Result.success(assetService.findStockByBatch(assetType, batch));
    }

    // ========== 统一列表（支持筛选） ==========

    @GetMapping("/list")
    public Result<List<Asset>> unifiedList(
            @RequestParam(required = false) String assetType,
            @RequestParam(required = false) String stockStatus,
            @RequestParam(required = false) Long batchId,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String keeper,
            @RequestParam(required = false) Integer status) {
        return Result.success(assetService.findWithFilters(assetType, stockStatus, batchId, department, keeper, status));
    }

    // ========== 状态变更 ==========

    @PutMapping("/{id}/status")
    public Result<?> changeStatus(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        int newStatus = Integer.parseInt(params.get("status").toString());
        assetService.changeStatus(id, newStatus);
        return Result.success();
    }

    @PutMapping("/batch-status")
    public Result<?> batchChangeStatus(@RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<Number> ids = (List<Number>) params.get("ids");
        int status = Integer.parseInt(params.get("status").toString());
        List<Long> longIds = ids.stream().map(Number::longValue).toList();
        assetService.batchChangeStatus(longIds, status);
        return Result.success();
    }

    @PutMapping("/batch-keeper")
    public Result<?> batchChangeKeeper(@RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<Number> ids = (List<Number>) params.get("ids");
        String department = (String) params.get("department");
        String keeper = (String) params.get("keeper");
        List<Long> longIds = ids.stream().map(Number::longValue).toList();
        assetService.batchChangeKeeper(longIds, department, keeper);
        return Result.success();
    }

    // ========== 批次管理 ==========

    @GetMapping("/batches")
    public Result<List<AssetBatch>> batchList(@RequestParam(required = false) String assetType) {
        if (assetType != null && !assetType.isEmpty()) {
            return Result.success(assetBatchService.findByAssetType(assetType));
        }
        return Result.success(assetBatchService.findAll());
    }

    @PostMapping("/batches")
    public Result<AssetBatch> createBatch(@RequestBody AssetBatch batch) {
        return Result.success(assetBatchService.create(batch));
    }

    // ========== 资产关联（电脑套装） ==========

    @GetMapping("/relations")
    public Result<List<AssetRelation>> relationList(@RequestParam Long hostId) {
        return Result.success(assetRelationService.findByHostId(hostId));
    }

    @PostMapping("/relations")
    public Result<AssetRelation> createRelation(@RequestBody AssetRelation relation) {
        return Result.success(assetRelationService.create(relation));
    }

    @DeleteMapping("/relations/{id}")
    public Result<?> deleteRelation(@PathVariable Long id) {
        assetRelationService.delete(id);
        return Result.success();
    }
}
