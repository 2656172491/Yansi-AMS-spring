package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.HostStockMapper;
import org.example.yansiamsspring.mapper.MonitorStockMapper;
import org.example.yansiamsspring.mapper.PcAssetMapper;
import org.example.yansiamsspring.pojo.PcAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PcAssetService {

    @Autowired
    private PcAssetMapper pcAssetMapper;
    @Autowired
    private HostStockMapper hostStockMapper;
    @Autowired
    private MonitorStockMapper monitorStockMapper;

    public List<PcAsset> findList(String computerNo, String hostSn, String monitorSn, String department, String keeper) {
        return pcAssetMapper.findList(computerNo, hostSn, monitorSn, department, keeper);
    }

    public PcAsset findById(Long id) {
        return pcAssetMapper.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(PcAsset pcAsset) {
        if (pcAsset.getStatus() == null) pcAsset.setStatus(1);
        pcAssetMapper.insert(pcAsset);
        // 将选中的主机和显示器标记为 assigned
        if (pcAsset.getHostId() != null) {
            hostStockMapper.updateStatus(pcAsset.getHostId(), "assigned");
        }
        if (pcAsset.getMonitorId() != null) {
            monitorStockMapper.updateStatus(pcAsset.getMonitorId(), "assigned");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(PcAsset pcAsset) {
        // 获取旧的配对信息
        Long oldHostId = pcAssetMapper.findHostIdById(pcAsset.getId());
        Long oldMonitorId = pcAssetMapper.findMonitorIdById(pcAsset.getId());

        pcAssetMapper.update(pcAsset);

        // 释放旧的主机/显示器（如果更换了）
        if (oldHostId != null && !oldHostId.equals(pcAsset.getHostId())) {
            hostStockMapper.updateStatus(oldHostId, "in_stock");
        }
        if (oldMonitorId != null && !oldMonitorId.equals(pcAsset.getMonitorId())) {
            monitorStockMapper.updateStatus(oldMonitorId, "in_stock");
        }
        // 标记新选择的为 assigned
        if (pcAsset.getHostId() != null && !pcAsset.getHostId().equals(oldHostId)) {
            hostStockMapper.updateStatus(pcAsset.getHostId(), "assigned");
        }
        if (pcAsset.getMonitorId() != null && !pcAsset.getMonitorId().equals(oldMonitorId)) {
            monitorStockMapper.updateStatus(pcAsset.getMonitorId(), "assigned");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void softDelete(Long id) {
        // 释放主机和显示器回库存
        Long hostId = pcAssetMapper.findHostIdById(id);
        Long monitorId = pcAssetMapper.findMonitorIdById(id);
        pcAssetMapper.softDelete(id);
        if (hostId != null) {
            hostStockMapper.updateStatus(hostId, "in_stock");
        }
        if (monitorId != null) {
            monitorStockMapper.updateStatus(monitorId, "in_stock");
        }
    }

    public List<String> findDepartments() {
        return pcAssetMapper.findDepartments();
    }

    public int count() {
        return pcAssetMapper.count();
    }
}
