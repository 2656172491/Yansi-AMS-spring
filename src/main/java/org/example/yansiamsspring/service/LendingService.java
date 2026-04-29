package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.AssetMapper;
import org.example.yansiamsspring.mapper.LendingRecordMapper;
import org.example.yansiamsspring.pojo.Asset;
import org.example.yansiamsspring.pojo.LendingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LendingService {

    @Autowired
    private LendingRecordMapper lendingMapper;

    @Autowired
    private AssetMapper assetMapper;

    public List<LendingRecord> findList(String status) {
        return lendingMapper.findList(status);
    }

    public LendingRecord findById(Long id) {
        return lendingMapper.findById(id);
    }

    public List<LendingRecord> findOverdue() {
        return lendingMapper.findOverdue();
    }

    @Transactional(rollbackFor = Exception.class)
    public void lend(LendingRecord record) {
        Asset asset = assetMapper.findById(record.getAssetId());
        if (asset == null) {
            throw new RuntimeException("设备不存在");
        }
        if (!"in_stock".equals(asset.getStockStatus()) && !"returned".equals(asset.getStockStatus())) {
            throw new RuntimeException("设备不在可借状态");
        }

        record.setStatus("lent");
        lendingMapper.insert(record);

        // 更新设备状态为使用中
        assetMapper.updateStockStatus(asset.getId(), "in_use", record.getBorrowerDept(), record.getBorrower());
    }

    @Transactional(rollbackFor = Exception.class)
    public void returnDevice(Long id) {
        LendingRecord record = lendingMapper.findById(id);
        if (record == null) {
            throw new RuntimeException("借出记录不存在");
        }
        if ("returned".equals(record.getStatus())) {
            throw new RuntimeException("设备已归还");
        }

        lendingMapper.returnDevice(id);

        // 更新设备状态为已回收
        assetMapper.updateStockStatus(record.getAssetId(), "returned", null, null);
    }
}
