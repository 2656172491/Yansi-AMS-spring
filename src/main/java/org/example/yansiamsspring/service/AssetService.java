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

    @Autowired
    private LogService logService;

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
    public void batchStockIn(List<Asset> assets, String operatorName, Long operatorId) {
        Long maxNo = assetMapper.getMaxComputerNo();
        long nextNo = (maxNo == null ? 1 : maxNo + 1);
        for (Asset asset : assets) {
            asset.setComputerNo(String.format("ASM-%05d", nextNo++));
            asset.setStockStatus("in_stock");
            asset.setStatus(1);
            asset.setDeleted(0);
            assetMapper.insert(asset);
            logService.recordFlow(asset.getId(), asset.getAssetType(), "in", 1,
                    asset.getPurchaseBatch(), operatorName, operatorId);
        }
        String type = assets.isEmpty() ? "" : assets.get(0).getAssetType();
        String batch = assets.isEmpty() ? "" : assets.get(0).getPurchaseBatch();
        logService.recordOperation(operatorId, null, operatorName, "入库", "asset", null,
                "入库了 " + assets.size() + " 台" + type + "设备，批次号 " + batch, true, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void assignAsset(Long assetId, String department, String keeper, String operatorName, Long operatorId) {
        Asset asset = assetMapper.findById(assetId);
        if (asset == null) {
            throw new RuntimeException("设备不存在");
        }
        if (!"in_stock".equals(asset.getStockStatus())) {
            throw new RuntimeException("该设备不在库存中");
        }
        assetMapper.updateStockStatus(assetId, "in_use", department, keeper);
        logService.recordFlow(assetId, asset.getAssetType(), "out", 1, null, operatorName, operatorId);
        logService.recordOperation(operatorId, null, operatorName, "配出", "asset", assetId,
                "将" + asset.getAssetType() + " " + asset.getComputerNo() + " 配出给" + department + keeper, true, null);
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

    public List<java.util.Map<String, Object>> getDepartmentStats() {
        return assetMapper.countByDepartment();
    }

    public List<java.util.Map<String, Object>> getStockBatch() {
        return assetMapper.countByBatch();
    }

    public List<Asset> findStockByBatch(String assetType, String batch) {
        return assetMapper.findStockByBatch(assetType, batch);
    }
}
