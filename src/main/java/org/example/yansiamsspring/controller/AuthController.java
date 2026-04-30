package org.example.yansiamsspring.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.yansiamsspring.pojo.Result;
import org.example.yansiamsspring.pojo.User;
import org.example.yansiamsspring.service.LogService;
import org.example.yansiamsspring.service.UserService;
import org.example.yansiamsspring.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private LogService logService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String username = params.get("username");
        String password = params.get("password");
        String ip = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        if (username == null || password == null) {
            return Result.error("用户名和密码不能为空");
        }

        User user = userService.findByUsername(username);
        if (user == null) {
            logService.recordLogin(null, username, null, ip, userAgent, false, "用户不存在");
            return Result.error("用户名或密码错误");
        }

        if (!userService.checkPassword(password, user.getPassword())) {
            logService.recordLogin(user.getId(), username, user.getName(), ip, userAgent, false, "密码错误");
            return Result.error("用户名或密码错误");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole(), user.getName());
        logService.recordLogin(user.getId(), username, user.getName(), ip, userAgent, true, null);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole(),
                "name", user.getName(),
                "email", user.getEmail() != null ? user.getEmail() : ""
        ));

        return Result.success(data);
    }

    @GetMapping("/me")
    public Result<?> me(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userService.findById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("role", user.getRole());
        data.put("name", user.getName());
        data.put("email", user.getEmail() != null ? user.getEmail() : "");
        return Result.success(data);
    }

    @PutMapping("/password")
    public Result<?> changePassword(HttpServletRequest request, @RequestBody Map<String, String> params) {
        Long userId = (Long) request.getAttribute("userId");
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");

        if (oldPassword == null || newPassword == null) {
            return Result.error("密码不能为空");
        }

        try {
            userService.changePassword(userId, oldPassword, newPassword);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip != null ? ip.split(",")[0].trim() : "unknown";
    }
}
