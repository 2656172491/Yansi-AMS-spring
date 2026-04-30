package org.example.yansiamsspring.controller;

import org.example.yansiamsspring.pojo.MeetingDevice;
import org.example.yansiamsspring.pojo.Result;
import org.example.yansiamsspring.service.MeetingDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meeting-devices")
public class MeetingDeviceController {

    @Autowired
    private MeetingDeviceService service;

    @GetMapping
    public Result<List<MeetingDevice>> list() {
        return Result.success(service.findAll());
    }

    @GetMapping("/{id}")
    public Result<MeetingDevice> detail(@PathVariable Long id) {
        MeetingDevice device = service.findById(id);
        if (device == null) return Result.error("记录不存在");
        return Result.success(device);
    }

    @PostMapping
    public Result<?> create(@RequestBody MeetingDevice device) {
        service.create(device);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody MeetingDevice device) {
        device.setId(id);
        service.update(device);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        service.softDelete(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<?> changeStatus(@PathVariable Long id, @RequestParam Integer status) {
        service.changeStatus(id, status);
        return Result.success();
    }
}
