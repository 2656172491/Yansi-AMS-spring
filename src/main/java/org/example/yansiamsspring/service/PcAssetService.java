package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.AssetMapper;
import org.example.yansiamsspring.mapper.PcAssetMapper;
import org.example.yansiamsspring.pojo.PcAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PcAssetService {

    @Autowired
    private PcAssetMapper pcAssetMapper;

    @Autowired
    private AssetMapper assetMapper;

    public List<PcAsset> findList(String computerNo, String hostSn, String monitorSn, String department, String keeper) {
        return pcAssetMapper.findList(computerNo, hostSn, monitorSn, department, keeper);
    }

    public PcAsset findById(Long id) {
        return pcAssetMapper.findById(id);
    }

    public void create(PcAsset pcAsset) {
        validateSn(pcAsset);
        if (pcAsset.getStatus() == null) pcAsset.setStatus(1);
        pcAssetMapper.insert(pcAsset);
    }

    public void update(PcAsset pcAsset) {
        validateSn(pcAsset);
        pcAssetMapper.update(pcAsset);
    }

    private void validateSn(PcAsset pcAsset) {
        if (pcAsset.getHostSn() != null && !pcAsset.getHostSn().isEmpty()) {
            if (assetMapper.countByHostSn(pcAsset.getHostSn()) == 0) {
                throw new RuntimeException("主机SN " + pcAsset.getHostSn() + " 在资产表中不存在");
            }
        }
        if (pcAsset.getMonitorSn() != null && !pcAsset.getMonitorSn().isEmpty()) {
            if (assetMapper.countByMonitorSn(pcAsset.getMonitorSn()) == 0) {
                throw new RuntimeException("显示器SN " + pcAsset.getMonitorSn() + " 在资产表中不存在");
            }
        }
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
