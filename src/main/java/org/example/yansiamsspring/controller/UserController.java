package org.example.yansiamsspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.yansiamsspring.pojo.Result;
import org.example.yansiamsspring.pojo.User;
import org.example.yansiamsspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Result<List<User>> list(HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.error("无权限");
        }
        List<User> users = userService.findAll();
        // 清除密码字段
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }

    @PostMapping
    public Result<?> create(HttpServletRequest request, @RequestBody User user) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.error("无权限");
        }

        if (user.getUsername() == null || user.getPassword() == null) {
            return Result.error("用户名和密码不能为空");
        }

        try {
            userService.createUser(user);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<?> update(HttpServletRequest request, @PathVariable Long id, @RequestBody User user) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.error("无权限");
        }

        user.setId(id);
        userService.updateUser(user);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    public Result<?> updateStatus(HttpServletRequest request, @PathVariable Long id, @RequestBody Map<String, Integer> params) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.error("无权限");
        }

        Integer status = params.get("status");
        if (status == null) {
            return Result.error("状态不能为空");
        }

        userService.updateStatus(id, status);
        return Result.success();
    }
}
