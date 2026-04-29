package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.PcAssetMapper;
import org.example.yansiamsspring.pojo.PcAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PcAssetService {

    @Autowired
    private PcAssetMapper pcAssetMapper;

    public List<PcAsset> findList(String computerNo, String hostSn, String monitorSn, String department, String keeper) {
        return pcAssetMapper.findList(computerNo, hostSn, monitorSn, department, keeper);
    }

    public PcAsset findById(Long id) {
        return pcAssetMapper.findById(id);
    }

    public void create(PcAsset pcAsset) {
        if (pcAsset.getStatus() == null) pcAsset.setStatus(1);
        pcAssetMapper.insert(pcAsset);
    }

    public void update(PcAsset pcAsset) {
        pcAssetMapper.update(pcAsset);
    }

    public void softDelete(Long id) {
        pcAssetMapper.softDelete(id);
    }

    public List<String> findDepartments() {
        return pcAssetMapper.findDepartments();
    }

    public int count() {
        return pcAssetMapper.count();
    }
}
