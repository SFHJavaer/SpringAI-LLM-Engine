package com.sfh.service;


import com.sfh.pojo.User;

/**
 * @ClassName UserService
 * @Author sfh
 * @Version 1.0
 * @Description 用户服务接口
 **/
public interface UserService {

    /**
     * 用户注册
     * @param username 用户名
     * @param email 邮箱
     * @param password 密码
     * @return User
     */
    User register(String username, String email, String password);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return User
     */
    User login(String username, String password);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return User
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * @return User
     */
    User findByEmail(String email);

    /**
     * 根据ID查找用户
     * @param id 用户ID
     * @return User
     */
    User findById(Long id);

    /**
     * 更新用户最后登录时间
     * @param userId 用户ID
     */
    void updateLastLoginTime(Long userId);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return boolean
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return boolean
     */
    boolean existsByEmail(String email);

    /**
     * 生成JWT token
     * @param user 用户
     * @return String
     */
    String generateToken(User user);

    /**
     * 验证JWT token
     * @param token token
     * @return User
     */
    User validateToken(String token);

    /**
     * 密码加密（BCrypt会自动生成盐值并包含在哈希中）
     * @param password 明文密码
     * @param salt 盐值（已废弃，BCrypt会自动处理）
     * @return String
     */
    String encryptPassword(String password, String salt);

    /**
     * 生成盐值（BCrypt会自动生成盐值并包含在哈希中）
     * @return String 返回null，盐值已包含在哈希中
     */
    String generateSalt();

    /**
     * 验证密码
     * @param plainPassword 明文密码
     * @param encryptedPassword 加密密码
     * @param salt 盐值
     * @return boolean
     */
    boolean verifyPassword(String plainPassword, String encryptedPassword, String salt);
}
