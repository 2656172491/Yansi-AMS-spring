package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.AssetRelationMapper;
import org.example.yansiamsspring.pojo.AssetRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetRelationService {

    @Autowired
    private AssetRelationMapper assetRelationMapper;

    public List<AssetRelation> findByHostId(Long hostId) {
        return assetRelationMapper.findByHostId(hostId);
    }

    public List<AssetRelation> findByRelatedId(Long relatedId) {
        return assetRelationMapper.findByRelatedId(relatedId);
    }

    public AssetRelation create(AssetRelation relation) {
        assetRelationMapper.insert(relation);
        return relation;
    }

    public void delete(Long id) {
        assetRelationMapper.delete(id);
    }

    public void deleteByHostAndType(Long hostId, String relationType) {
        assetRelationMapper.deleteByHostAndType(hostId, relationType);
    }
}
