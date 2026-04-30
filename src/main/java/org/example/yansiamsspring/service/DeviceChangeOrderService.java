package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.DeviceChangeOrderMapper;
import org.example.yansiamsspring.mapper.PcAssetMapper;
import org.example.yansiamsspring.pojo.DeviceChangeOrder;
import org.example.yansiamsspring.pojo.PcAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceChangeOrderService {

    @Autowired
    private DeviceChangeOrderMapper orderMapper;

    @Autowired
    private PcAssetMapper pcAssetMapper;

    public List<DeviceChangeOrder> findList(String status, Long handlerId) {
        return orderMapper.findList(status, handlerId);
    }

    public DeviceChangeOrder findById(Long id) {
        return orderMapper.findById(id);
    }

    public void createOrder(DeviceChangeOrder order) {
        order.setStatus("done");
        order.setResult("auto");
        order.setOrderNo("CHG-" + System.currentTimeMillis());
        orderMapper.insert(order);
    }

    public void updateStatus(Long id, String status, String remark) {
        orderMapper.updateStatus(id, status, remark);
    }

    @Transactional(rollbackFor = Exception.class)
    public void completeOrder(Long id, String result, Long newAssetId, String remark) {
        DeviceChangeOrder order = orderMapper.findById(id);
        if (order == null) {
            throw new RuntimeException("工单不存在");
        }
        if ("done".equals(order.getStatus())) {
            throw new RuntimeException("工单已完成");
        }
        orderMapper.complete(id, result, newAssetId, remark);
    }

    public List<java.util.Map<String, Object>> getOrderStats() {
        return orderMapper.countByMonth();
    }

    public List<java.util.Map<String, Object>> getTypeStats(int days) {
        return orderMapper.countByType(days);
    }
}
