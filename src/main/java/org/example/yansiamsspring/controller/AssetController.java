package org.example.yansiamsspring.controller;

import org.example.yansiamsspring.pojo.Asset;
import org.example.yansiamsspring.pojo.Result;
import org.example.yansiamsspring.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        if (asset == null) return Result.error("记录不存在");
        return Result.success(asset);
    }

    @PostMapping
    public Result<?> create(@RequestBody Asset asset) {
        assetService.create(asset);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Asset asset) {
        asset.setId(id);
        assetService.update(asset);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        assetService.softDelete(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<?> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        assetService.changeStatus(id, status);
        return Result.success();
    }
}
