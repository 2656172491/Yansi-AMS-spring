package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.AssetMapper;
import org.example.yansiamsspring.pojo.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Asset findByAssetType(String assetType) {
        return assetMapper.findByAssetType(assetType);
    }

    public void create(Asset asset) {
        if (asset.getStatus() == null) asset.setStatus(1);
        if (asset.getQuantity() == null) asset.setQuantity(0);
        if (asset.getInUseQuantity() == null) asset.setInUseQuantity(0);
        if (asset.getWarningQuantity() == null) asset.setWarningQuantity(-1);
        assetMapper.insert(asset);
    }

    public void update(Asset asset) {
        assetMapper.update(asset);
    }

    public void softDelete(Long id) {
        assetMapper.softDelete(id);
    }

    public void changeStatus(Long id, Integer status) {
        assetMapper.updateStatus(id, status);
    }
}
