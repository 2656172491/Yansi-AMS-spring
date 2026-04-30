package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.AssetBatchMapper;
import org.example.yansiamsspring.pojo.AssetBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetBatchService {

    @Autowired
    private AssetBatchMapper assetBatchMapper;

    public List<AssetBatch> findAll() {
        return assetBatchMapper.findAll();
    }

    public AssetBatch findById(Long id) {
        return assetBatchMapper.findById(id);
    }

    public List<AssetBatch> findByAssetType(String assetType) {
        return assetBatchMapper.findByAssetType(assetType);
    }

    public AssetBatch create(AssetBatch batch) {
        assetBatchMapper.insert(batch);
        return batch;
    }

    public void update(AssetBatch batch) {
        assetBatchMapper.update(batch);
    }
}
