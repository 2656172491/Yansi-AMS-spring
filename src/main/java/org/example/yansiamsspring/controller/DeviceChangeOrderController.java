package org.example.yansiamsspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.yansiamsspring.pojo.DeviceChangeOrder;
import org.example.yansiamsspring.pojo.Result;
import org.example.yansiamsspring.service.DeviceChangeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/change-orders")
public class DeviceChangeOrderController {

    @Autowired
    private DeviceChangeOrderService orderService;

    @GetMapping
    public Result<List<DeviceChangeOrder>> list(
            HttpServletRequest request,
            @RequestParam(required = false) String status) {
        String role = (String) request.getAttribute("role");
        Long userId = (Long) request.getAttribute("userId");

        // 学生助理只能看自己的工单
        if ("assistant".equals(role)) {
            return Result.success(orderService.findList(status, userId));
        }
        return Result.success(orderService.findList(status, null));
    }

    @GetMapping("/{id}")
    public Result<DeviceChangeOrder> detail(@PathVariable Long id) {
        DeviceChangeOrder order = orderService.findById(id);
        if (order == null) {
            return Result.error("工单不存在");
        }
        return Result.success(order);
    }

    @PostMapping
    public Result<?> create(HttpServletRequest request, @RequestBody DeviceChangeOrder order) {
        String role = (String) request.getAttribute("role");
        if (!"assistant".equals(role) && !"admin".equals(role)) {
            return Result.error("无权限创建工单");
        }

        Long userId = (Long) request.getAttribute("userId");
        String name = (String) request.getAttribute("name");

        order.setHandlerId(userId);
        order.setHandler(name);

        try {
            orderService.createOrder(order);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/check")
    public Result<?> updateStatus(HttpServletRequest request, @PathVariable Long id, @RequestBody Map<String, String> params) {
        String role = (String) request.getAttribute("role");
        if (!"assistant".equals(role) && !"admin".equals(role)) {
            return Result.error("无权限");
        }

        String status = params.get("status");
        String remark = params.get("remark");

        orderService.updateStatus(id, status, remark);
        return Result.success();
    }

    @PutMapping("/{id}/complete")
    public Result<?> complete(HttpServletRequest request, @PathVariable Long id, @RequestBody Map<String, Object> params) {
        String role = (String) request.getAttribute("role");
        if (!"teacher".equals(role) && !"admin".equals(role)) {
            return Result.error("无权限完成工单");
        }

        String result = (String) params.get("result");
        Long newAssetId = params.get("newAssetId") != null ? Long.valueOf(params.get("newAssetId").toString()) : null;
        String remark = (String) params.get("remark");

        try {
            orderService.completeOrder(id, result, newAssetId, remark);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/stats")
    public Result<List<Map<String, Object>>> stats() {
        return Result.success(orderService.getOrderStats());
    }
}
