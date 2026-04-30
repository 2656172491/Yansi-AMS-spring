package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.*;
import org.example.yansiamsspring.pojo.AssetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssetTypeService {

    @Autowired
    private AssetTypeMapper assetTypeMapper;
    @Autowired
    private AssetMapper assetMapper;
    @Autowired
    private AssetBatchMapper assetBatchMapper;
    @Autowired
    private AssetFlowLogMapper assetFlowLogMapper;
    @Autowired
    private DeviceChangeOrderMapper deviceChangeOrderMapper;
    @Autowired
    private LendingRecordMapper lendingRecordMapper;

    public List<AssetType> findAll() {
        return assetTypeMapper.findAll();
    }

    public List<AssetType> findActive() {
        return assetTypeMapper.findActive();
    }

    public void create(AssetType assetType) {
        AssetType existing = assetTypeMapper.findByCode(assetType.getCode());
        if (existing != null) {
            throw new RuntimeException("类型编码已存在");
        }
        if (assetType.getStatus() == null) assetType.setStatus(1);
        if (assetType.getSortOrder() == null) assetType.setSortOrder(0);
        assetTypeMapper.insert(assetType);
    }

    public void update(AssetType assetType) {
        AssetType existing = assetTypeMapper.findByCode(assetType.getCode());
        if (existing != null && !existing.getId().equals(assetType.getId())) {
            throw new RuntimeException("类型编码已存在");
        }
        assetTypeMapper.update(assetType);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AssetType type = assetTypeMapper.findById(id);
        if (type == null) {
            throw new RuntimeException("设备类型不存在");
        }
        String code = type.getCode();
        // 先查出该类型下的资产ID列表，用于删借出记录
        List<Long> assetIds = assetMapper.findIdsByAssetType(code);
        if (!assetIds.isEmpty()) {
            lendingRecordMapper.deleteByAssetIds(assetIds);
        }
        assetMapper.deleteByAssetType(code);
        assetBatchMapper.deleteByAssetType(code);
        assetFlowLogMapper.deleteByAssetType(code);
        deviceChangeOrderMapper.deleteByAssetCategory(code);
        assetTypeMapper.delete(id);
    }
}
