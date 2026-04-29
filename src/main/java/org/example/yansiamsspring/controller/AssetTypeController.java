package org.example.yansiamsspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.yansiamsspring.pojo.AssetType;
import org.example.yansiamsspring.pojo.Result;
import org.example.yansiamsspring.service.AssetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset-types")
public class AssetTypeController {

    @Autowired
    private AssetTypeService assetTypeService;

    @GetMapping
    public Result<List<AssetType>> list(@RequestParam(required = false) String mode) {
        if ("active".equals(mode)) {
            return Result.success(assetTypeService.findActive());
        }
        return Result.success(assetTypeService.findAll());
    }

    @PostMapping
    public Result<?> create(HttpServletRequest request, @RequestBody AssetType assetType) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.error("无权限");
        }
        try {
            assetTypeService.create(assetType);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<?> update(HttpServletRequest request, @PathVariable Long id, @RequestBody AssetType assetType) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.error("无权限");
        }
        assetType.setId(id);
        try {
            assetTypeService.update(assetType);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(HttpServletRequest request, @PathVariable Long id) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.error("无权限");
        }
        assetTypeService.delete(id);
        return Result.success();
    }
}
