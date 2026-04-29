package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.AssetTypeMapper;
import org.example.yansiamsspring.pojo.AssetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetTypeService {

    @Autowired
    private AssetTypeMapper assetTypeMapper;

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

    public void delete(Long id) {
        assetTypeMapper.delete(id);
    }
}
