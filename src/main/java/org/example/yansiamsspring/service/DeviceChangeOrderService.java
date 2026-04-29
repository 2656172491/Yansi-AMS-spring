package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.AssetMapper;
import org.example.yansiamsspring.mapper.DeviceChangeOrderMapper;
import org.example.yansiamsspring.pojo.Asset;
import org.example.yansiamsspring.pojo.DeviceChangeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceChangeOrderService {

    @Autowired
    private DeviceChangeOrderMapper orderMapper;

    @Autowired
    private AssetMapper assetMapper;

    public List<DeviceChangeOrder> findList(String status, Long handlerId) {
        return orderMapper.findList(status, handlerId);
    }

    public DeviceChangeOrder findById(Long id) {
        return orderMapper.findById(id);
    }

    public void createOrder(DeviceChangeOrder order) {
        order.setStatus("pending");
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

        // 更新工单状态
        orderMapper.complete(id, result, newAssetId, remark);

        // 处理设备状态
        if ("replaced".equals(result) && newAssetId != null) {
            // 更换：原设备→returned，新设备→in_use
            Asset oldAsset = assetMapper.findById(order.getAssetId());
            if (oldAsset != null) {
                assetMapper.updateStockStatus(oldAsset.getId(), "returned", null, null);
            }
            Asset newAsset = assetMapper.findById(newAssetId);
            if (newAsset != null) {
                assetMapper.updateStockStatus(newAssetId, "in_use",
                        oldAsset != null ? oldAsset.getDepartment() : null,
                        oldAsset != null ? oldAsset.getKeeper() : null);
            }
        } else if ("recycled".equals(result)) {
            // 回收：原设备→returned
            assetMapper.updateStockStatus(order.getAssetId(), "returned", null, null);
        }
        // repaired: 维修后归还，设备状态不变
    }
}
