package com.sfh.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.sfh.entity.UserEntity;
import com.sfh.pojo.User;
import com.sfh.repository.UserRepository;
import com.sfh.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

/**
 * @ClassName UserServiceImpl
 * @Author sfh
 * @Version 1.0
 * @Description 用户服务实现类
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(String username, String email, String password) {
        log.info("开始注册用户: {}", username);

        // 生成加密密码（BCrypt会自动生成盐值并包含在哈希中）
        String encryptedPassword = encryptPassword(password, null);

        // BCrypt已经将盐值包含在哈希中，这里使用空字符串
        String salt = "";

        // 创建用户实体
        UserEntity userEntity = new UserEntity(username, email, encryptedPassword, salt);
        userEntity.setCreateTime(LocalDateTime.now());
        userEntity.setUpdateTime(LocalDateTime.now());

        // 保存到数据库
        UserEntity savedUser = userRepository.save(userEntity);

        log.info("用户注册成功: {}", username);

        // 转换为Bean对象返回
        return convertToUser(savedUser);
    }

    @Override
    public User login(String username, String password) {
        log.info("用户登录尝试: {}", username);

        // 根据用户名或邮箱查找用户
        Optional<UserEntity> userOpt = userRepository.findByUsernameOrEmail(username, username);

        if (userOpt.isEmpty()) {
            log.warn("用户不存在: {}", username);
            return null;
        }

        UserEntity userEntity = userOpt.get();

        // 验证密码
        if (!verifyPassword(password, userEntity.getPassword(), userEntity.getSalt())) {
            log.warn("密码错误: {}", username);
            return null;
        }

        // 更新最后登录时间
        userEntity.updateLastLoginTime();
        userRepository.save(userEntity);

        log.info("用户登录成功: {}", username);

        return convertToUser(userEntity);
    }

    @Override
    public User findByUsername(String username) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        return userOpt.map(this::convertToUser).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        return userOpt.map(this::convertToUser).orElse(null);
    }

    @Override
    public User findById(Long id) {
        Optional<UserEntity> userOpt = userRepository.findById(id);
        return userOpt.map(this::convertToUser).orElse(null);
    }

    @Override
    public void updateLastLoginTime(Long userId) {
        userRepository.updateLastLoginTime(userId, LocalDateTime.now(), LocalDateTime.now());
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        // 使用HS512算法创建密钥
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    @Override
    public User validateToken(String token) {
        try {
            // 使用HS512算法创建密钥
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = Long.parseLong(claims.getSubject());
            return findById(userId);
        } catch (Exception e) {
            log.error("Token验证失败: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String encryptPassword(String password, String salt) {
        // 使用BCrypt加密，盐值会自动生成并包含在哈希结果中
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public String generateSalt() {
        // BCrypt会自动生成盐值并包含在哈希中，这里返回null
        return null;
    }

    @Override
    public boolean verifyPassword(String plainPassword, String encryptedPassword, String salt) {
        try {
            // BCrypt验证时不需要单独的盐值，因为盐值已经包含在哈希值中
            // salt参数为了接口兼容性保留，但实际不使用
            if (encryptedPassword == null || encryptedPassword.trim().isEmpty()) {
                log.error("加密密码为空");
                return false;
            }
            return BCrypt.checkpw(plainPassword, encryptedPassword);
        } catch (Exception e) {
            log.error("密码验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 将UserEntity转换为User Bean
     * @param userEntity 用户实体
     * @return User
     */
    private User convertToUser(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setEmail(userEntity.getEmail());
        user.setPassword(userEntity.getPassword());
        user.setSalt(userEntity.getSalt());
        user.setStatus(userEntity.getStatus());
        user.setCreateTime(userEntity.getCreateTime());
        user.setUpdateTime(userEntity.getUpdateTime());
        user.setLastLoginTime(userEntity.getLastLoginTime());
        user.setAvatar(userEntity.getAvatar());
        user.setPhone(userEntity.getPhone());
        user.setRealName(userEntity.getRealName());
        user.setGender(userEntity.getGender());
        user.setBirthday(userEntity.getBirthday());
        user.setAddress(userEntity.getAddress());
        user.setBio(userEntity.getBio());
        user.setWebsite(userEntity.getWebsite());
        user.setRole(userEntity.getRole());

        return user;
    }
}
