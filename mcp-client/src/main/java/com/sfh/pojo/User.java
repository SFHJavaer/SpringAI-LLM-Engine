package com.sfh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @ClassName User
 * @Author sfh
 * @Version 1.0
 * @Description 用户实体类
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String salt;
    private Integer status; // 0:未激活, 1:正常, 2:禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime lastLoginTime;
    private String avatar;
    private String phone;
    private String realName;
    private Integer gender; // 0:未知, 1:男, 2:女
    private LocalDateTime birthday;
    private String address;
    private String bio;
    private String website;
    private String role; // user, admin, vip

    // 扩展字段（不存数据库）
    private String token;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = 1;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.role = "user";
    }
}
