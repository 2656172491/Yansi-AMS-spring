package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.NetworkDeviceMapper;
import org.example.yansiamsspring.pojo.NetworkDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NetworkDeviceService {

    @Autowired
    private NetworkDeviceMapper mapper;

    public List<NetworkDevice> findAll() { return mapper.findAll(); }

    public NetworkDevice findById(Long id) { return mapper.findById(id); }

    public void create(NetworkDevice device) {
        if (device.getStatus() == null) device.setStatus(1);
        mapper.insert(device);
    }

    public void update(NetworkDevice device) { mapper.update(device); }

    public void softDelete(Long id) { mapper.softDelete(id); }

    public void changeStatus(Long id, Integer status) { mapper.updateStatus(id, status); }
}
