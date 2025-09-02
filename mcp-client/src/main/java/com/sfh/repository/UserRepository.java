package com.sfh.repository;

import com.sfh.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName UserRepository
 * @Author sfh
 * @Version 1.0
 * @Description 用户数据访问层
 **/
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return Optional<UserEntity>
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * @return Optional<UserEntity>
     */
    Optional<UserEntity> findByEmail(String email);

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
     * 根据用户名或邮箱查找用户（登录用）
     * @param username 用户名
     * @param email 邮箱
     * @return Optional<UserEntity>
     */
    @Query("SELECT u FROM UserEntity u WHERE u.username = :username OR u.email = :email")
    Optional<UserEntity> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    /**
     * 更新用户最后登录时间
     * @param userId 用户ID
     * @param lastLoginTime 最后登录时间
     */
    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.lastLoginTime = :lastLoginTime, u.updateTime = :updateTime WHERE u.id = :userId")
    void updateLastLoginTime(@Param("userId") Long userId,
                           @Param("lastLoginTime") LocalDateTime lastLoginTime,
                           @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据状态查找用户
     * @param status 状态
     * @return List<UserEntity>
     */
    List<UserEntity> findByStatus(Integer status);

    /**
     * 根据角色查找用户
     * @param role 角色
     * @return List<UserEntity>
     */
    List<UserEntity> findByRole(String role);

    /**
     * 统计用户总数
     * @return Long
     */
    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.status = 1")
    Long countActiveUsers();

    /**
     * 根据创建时间范围查找用户
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<UserEntity>
     */
    @Query("SELECT u FROM UserEntity u WHERE u.createTime BETWEEN :startTime AND :endTime")
    List<UserEntity> findByCreateTimeBetween(@Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);
}
