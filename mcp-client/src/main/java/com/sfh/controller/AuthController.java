package com.sfh.controller;


import com.sfh.pojo.AuthResponse;
import com.sfh.pojo.LoginRequest;
import com.sfh.pojo.RegisterRequest;
import com.sfh.pojo.User;
import com.sfh.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName AuthController
 * @Author sfh
 * @Version 1.0
 * @Description 认证控制器
 **/
@RestController
@RequestMapping("auth")
@Slf4j
public class AuthController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            log.info("用户注册请求: {}", request.getUsername());
            log.info("注册请求详情: {}", request);

            // 验证请求参数
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "用户名不能为空"));
            }

            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "邮箱不能为空"));
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "密码不能为空"));
            }

            if (!request.getPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "两次输入的密码不一致"));
            }

            // 检查用户名是否已存在
            if (userService.existsByUsername(request.getUsername())) {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "用户名已存在"));
            }

            // 检查邮箱是否已存在
            if (userService.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "邮箱已被注册"));
            }

            // 注册用户
            User user = userService.register(request.getUsername(), request.getEmail(), request.getPassword());
            String token = userService.generateToken(user);

            log.info("用户注册成功: {}", user.getUsername());

            return ResponseEntity.ok(new AuthResponse(true, "注册成功", token, user));

        } catch (Exception e) {
            log.error("用户注册失败", e);
            return ResponseEntity.internalServerError()
                .body(new AuthResponse(false, "注册失败，请稍后重试"));
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            log.info("用户登录请求: {}", request.getUsername());

            // 验证请求参数
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "用户名或邮箱不能为空"));
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "密码不能为空"));
            }

            // 登录验证
            User user = userService.login(request.getUsername(), request.getPassword());

            if (user == null) {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "用户名或密码错误"));
            }

            // 检查用户状态
            if (user.getStatus() != null && user.getStatus() != 1) {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "账户已被禁用"));
            }

            // 更新最后登录时间
            userService.updateLastLoginTime(user.getId());

            // 生成token
            String token = userService.generateToken(user);
            user.setToken(token);

            log.info("用户登录成功: {}", user.getUsername());

            return ResponseEntity.ok(new AuthResponse(true, "登录成功", token, user));

        } catch (Exception e) {
            log.error("用户登录失败", e);
            return ResponseEntity.internalServerError()
                .body(new AuthResponse(false, "登录失败，请稍后重试"));
        }
    }

    /**
     * 验证token
     */
    @GetMapping("validate")
    public ResponseEntity<AuthResponse> validateToken(@RequestParam String token) {
        try {
            User user = userService.validateToken(token);

            if (user != null) {
                return ResponseEntity.ok(new AuthResponse(true, "token有效", token, user));
            } else {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "token无效或已过期"));
            }

        } catch (Exception e) {
            log.error("token验证失败", e);
            return ResponseEntity.internalServerError()
                .body(new AuthResponse(false, "验证失败"));
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("current")
    public ResponseEntity<AuthResponse> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "无效的认证信息"));
            }

            String token = authHeader.substring(7);
            User user = userService.validateToken(token);

            if (user != null) {
                return ResponseEntity.ok(new AuthResponse(true, "获取成功", token, user));
            } else {
                return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "用户未登录"));
            }

        } catch (Exception e) {
            log.error("获取当前用户信息失败", e);
            return ResponseEntity.internalServerError()
                .body(new AuthResponse(false, "获取失败"));
        }
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("check-username")
    public ResponseEntity<AuthResponse> checkUsername(@RequestParam String username) {
        try {
            boolean exists = userService.existsByUsername(username);
            if (exists) {
                return ResponseEntity.ok(new AuthResponse(false, "用户名已存在"));
            } else {
                return ResponseEntity.ok(new AuthResponse(true, "用户名可用"));
            }
        } catch (Exception e) {
            log.error("检查用户名失败", e);
            return ResponseEntity.internalServerError()
                .body(new AuthResponse(false, "检查失败"));
        }
    }

    /**
     * 检查邮箱是否存在
     */
    @GetMapping("check-email")
    public ResponseEntity<AuthResponse> checkEmail(@RequestParam String email) {
        try {
            boolean exists = userService.existsByEmail(email);
            if (exists) {
                return ResponseEntity.ok(new AuthResponse(false, "邮箱已被注册"));
            } else {
                return ResponseEntity.ok(new AuthResponse(true, "邮箱可用"));
            }
        } catch (Exception e) {
            log.error("检查邮箱失败", e);
            return ResponseEntity.internalServerError()
                .body(new AuthResponse(false, "检查失败"));
        }
    }

    /**
     * 测试数据库连接
     */
    @GetMapping("test-db")
    public ResponseEntity<AuthResponse> testDatabase() {
        try {
            log.info("测试数据库连接...");
            boolean usernameExists = userService.existsByUsername("test");
            log.info("数据库连接正常，测试用户名存在性: {}", usernameExists);
            return ResponseEntity.ok(new AuthResponse(true, "数据库连接正常"));
        } catch (Exception e) {
            log.error("数据库连接失败", e);
            return ResponseEntity.internalServerError()
                .body(new AuthResponse(false, "数据库连接失败: " + e.getMessage()));
        }
    }
}
