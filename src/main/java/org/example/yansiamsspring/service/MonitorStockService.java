package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.MonitorStockMapper;
import org.example.yansiamsspring.pojo.MonitorStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorStockService {

    @Autowired
    private MonitorStockMapper monitorStockMapper;

    public List<MonitorStock> findList(String status) {
        return monitorStockMapper.findList(status);
    }

    public MonitorStock findById(Long id) {
        return monitorStockMapper.findById(id);
    }

    public void create(MonitorStock monitorStock) {
        MonitorStock existing = monitorStockMapper.findBySn(monitorStock.getSn());
        if (existing != null) {
            throw new RuntimeException("SN已存在");
        }
        if (monitorStock.getStatus() == null) monitorStock.setStatus("in_stock");
        monitorStockMapper.insert(monitorStock);
    }

    public void update(MonitorStock monitorStock) {
        MonitorStock existing = monitorStockMapper.findBySn(monitorStock.getSn());
        if (existing != null && !existing.getId().equals(monitorStock.getId())) {
            throw new RuntimeException("SN已存在");
        }
        monitorStockMapper.update(monitorStock);
    }

    public void delete(Long id) {
        MonitorStock monitor = monitorStockMapper.findById(id);
        if (monitor == null) throw new RuntimeException("记录不存在");
        if ("assigned".equals(monitor.getStatus())) {
            throw new RuntimeException("已分配的显示器不能删除");
        }
        monitorStockMapper.delete(id);
    }

    public void updateStatus(Long id, String status) {
        monitorStockMapper.updateStatus(id, status);
    }
}
