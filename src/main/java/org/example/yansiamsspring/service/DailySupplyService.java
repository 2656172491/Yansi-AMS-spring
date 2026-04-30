package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.DailySupplyMapper;
import org.example.yansiamsspring.pojo.DailySupply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailySupplyService {

    @Autowired
    private DailySupplyMapper mapper;

    public List<DailySupply> findAll() { return mapper.findAll(); }

    public DailySupply findById(Long id) { return mapper.findById(id); }

    public void create(DailySupply supply) {
        if (supply.getStatus() == null) supply.setStatus(1);
        if (supply.getQuantity() == null) supply.setQuantity(0);
        if (supply.getWarningQuantity() == null) supply.setWarningQuantity(0);
        mapper.insert(supply);
    }

    public void update(DailySupply supply) { mapper.update(supply); }

    public void softDelete(Long id) { mapper.softDelete(id); }

    public void changeStatus(Long id, Integer status) { mapper.updateStatus(id, status); }
}
