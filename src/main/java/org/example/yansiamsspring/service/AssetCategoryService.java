package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.AssetCategoryMapper;
import org.example.yansiamsspring.pojo.AssetCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetCategoryService {

    @Autowired
    private AssetCategoryMapper mapper;

    public List<AssetCategory> findAll() { return mapper.findAll(); }
    public List<AssetCategory> findActive() { return mapper.findActive(); }
    public AssetCategory findByCode(String code) { return mapper.findByCode(code); }
    public void create(AssetCategory c) { mapper.insert(c); }
    public void update(AssetCategory c) { mapper.update(c); }
    public void delete(Long id) { mapper.delete(id); }
}
