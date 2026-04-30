package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.CameraDeviceMapper;
import org.example.yansiamsspring.pojo.CameraDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CameraDeviceService {

    @Autowired
    private CameraDeviceMapper mapper;

    public List<CameraDevice> findAll() { return mapper.findAll(); }

    public CameraDevice findById(Long id) { return mapper.findById(id); }

    public void create(CameraDevice device) {
        if (device.getStatus() == null) device.setStatus(1);
        mapper.insert(device);
    }

    public void update(CameraDevice device) { mapper.update(device); }

    public void softDelete(Long id) { mapper.softDelete(id); }

    public void changeStatus(Long id, Integer status) { mapper.updateStatus(id, status); }
}
