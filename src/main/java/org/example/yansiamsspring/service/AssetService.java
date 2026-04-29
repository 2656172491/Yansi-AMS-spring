package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.AssetMapper;
import org.example.yansiamsspring.pojo.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssetService {

    @Autowired
    private AssetMapper assetMapper;

    public List<Asset> findAll() {
        return assetMapper.findAll();
    }

    public Asset findById(Long id) {
        return assetMapper.findById(id);
    }

    public List<Asset> findStockPool(String assetType) {
        if (assetType != null && !assetType.isEmpty()) {
            return assetMapper.findStockPoolByType(assetType);
        }
        return assetMapper.findStockPool();
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchStockIn(List<Asset> assets) {
        for (Asset asset : assets) {
            asset.setStockStatus("in_stock");
            asset.setStatus(1);
            asset.setDeleted(0);
            assetMapper.insert(asset);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void assignAsset(Long assetId, String department, String keeper) {
        Asset asset = assetMapper.findById(assetId);
        if (asset == null) {
            throw new RuntimeException("设备不存在");
        }
        if (!"in_stock".equals(asset.getStockStatus())) {
            throw new RuntimeException("该设备不在库存中");
        }
        assetMapper.updateStockStatus(assetId, "in_use", department, keeper);
    }

    public void updateAsset(Asset asset) {
        assetMapper.update(asset);
    }

    public void softDelete(Long id) {
        assetMapper.softDelete(id);
    }

    public List<String> findDepartments() {
        return assetMapper.findDepartments();
    }

    public List<java.util.Map<String, Object>> getStatistics() {
        return assetMapper.countByTypeAndStatus();
    }
}
