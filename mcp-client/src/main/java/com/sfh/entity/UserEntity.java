package com.sfh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * @ClassName UserEntity
 * @Author sfh
 * @Version 1.0
 * @Description 用户JPA实体类
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user", indexes = {
    @Index(name = "idx_username", columnList = "username", unique = true),
    @Index(name = "idx_email", columnList = "email", unique = true),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_create_time", columnList = "create_time")
})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "salt", nullable = false, length = 64)
    private String salt = "";

    @Column(name = "status", nullable = false)
    private Integer status = 1; // 0:未激活, 1:正常, 2:禁用

    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "real_name", length = 50)
    private String realName;

    @Column(name = "gender")
    private Integer gender; // 0:未知, 1:男, 2:女

    @Column(name = "birthday")
    private LocalDateTime birthday;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "website", length = 255)
    private String website;

    @Column(name = "role", nullable = false, length = 20)
    private String role = "user"; // user, admin, vip

    // 构造函数
    public UserEntity(String username, String email, String password, String salt) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.salt = salt != null ? salt : "";
        this.status = 1;
        this.role = "user";
    }

    // 更新最后登录时间
    public void updateLastLoginTime() {
        this.lastLoginTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    // 检查用户是否激活
    public boolean isActive() {
        return this.status != null && this.status == 1;
    }

    // 检查是否为管理员
    public boolean isAdmin() {
        return "admin".equals(this.role);
    }

    // 检查是否为VIP用户
    public boolean isVip() {
        return "vip".equals(this.role) || "admin".equals(this.role);
    }
}
