package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.LendingRecordMapper;
import org.example.yansiamsspring.mapper.PcAssetMapper;
import org.example.yansiamsspring.pojo.LendingRecord;
import org.example.yansiamsspring.pojo.PcAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LendingService {

    @Autowired
    private LendingRecordMapper lendingMapper;

    @Autowired
    private PcAssetMapper pcAssetMapper;

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
        PcAsset asset = pcAssetMapper.findById(record.getAssetId());
        if (asset == null) {
            throw new RuntimeException("设备不存在");
        }
        record.setStatus("lent");
        lendingMapper.insert(record);
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
    }

    public List<java.util.Map<String, Object>> getLendingStats() {
        return lendingMapper.countByMonth();
    }
}
