package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.MeetingDeviceMapper;
import org.example.yansiamsspring.pojo.MeetingDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingDeviceService {

    @Autowired
    private MeetingDeviceMapper mapper;

    public List<MeetingDevice> findAll() { return mapper.findAll(); }

    public MeetingDevice findById(Long id) { return mapper.findById(id); }

    public void create(MeetingDevice device) {
        if (device.getStatus() == null) device.setStatus(1);
        mapper.insert(device);
    }

    public void update(MeetingDevice device) { mapper.update(device); }

    public void softDelete(Long id) { mapper.softDelete(id); }

    public void changeStatus(Long id, Integer status) { mapper.updateStatus(id, status); }
}
