package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.HostStockMapper;
import org.example.yansiamsspring.pojo.HostStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostStockService {

    @Autowired
    private HostStockMapper hostStockMapper;

    public List<HostStock> findList(String status) {
        return hostStockMapper.findList(status);
    }

    public HostStock findById(Long id) {
        return hostStockMapper.findById(id);
    }

    public void create(HostStock hostStock) {
        HostStock existing = hostStockMapper.findBySn(hostStock.getSn());
        if (existing != null) {
            throw new RuntimeException("SN已存在");
        }
        if (hostStock.getStatus() == null) hostStock.setStatus("in_stock");
        hostStockMapper.insert(hostStock);
    }

    public void update(HostStock hostStock) {
        HostStock existing = hostStockMapper.findBySn(hostStock.getSn());
        if (existing != null && !existing.getId().equals(hostStock.getId())) {
            throw new RuntimeException("SN已存在");
        }
        hostStockMapper.update(hostStock);
    }

    public void delete(Long id) {
        HostStock host = hostStockMapper.findById(id);
        if (host == null) throw new RuntimeException("记录不存在");
        if ("assigned".equals(host.getStatus())) {
            throw new RuntimeException("已分配的主机不能删除");
        }
        hostStockMapper.delete(id);
    }

    public void updateStatus(Long id, String status) {
        hostStockMapper.updateStatus(id, status);
    }
}
