package com.sfh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName LoginRequest
 * @Author sfh
 * @Version 1.0
 * @Description 登录请求实体
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String username;
    private String password;
    private Boolean rememberMe;
}
