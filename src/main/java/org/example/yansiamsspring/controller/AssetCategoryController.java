package org.example.yansiamsspring.controller;

import org.example.yansiamsspring.pojo.AssetCategory;
import org.example.yansiamsspring.pojo.Result;
import org.example.yansiamsspring.service.AssetCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset-categories")
public class AssetCategoryController {

    @Autowired
    private AssetCategoryService service;

    @GetMapping
    public Result<List<AssetCategory>> list(@RequestParam(required = false) String mode) {
        if ("active".equals(mode)) {
            return Result.success(service.findActive());
        }
        return Result.success(service.findAll());
    }

    @GetMapping("/{code}")
    public Result<AssetCategory> getByCode(@PathVariable String code) {
        AssetCategory c = service.findByCode(code);
        if (c == null) return Result.error("大类不存在");
        return Result.success(c);
    }

    @PostMapping
    public Result<?> create(@RequestBody AssetCategory category) {
        service.create(category);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody AssetCategory category) {
        category.setId(id);
        service.update(category);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.success();
    }
}
