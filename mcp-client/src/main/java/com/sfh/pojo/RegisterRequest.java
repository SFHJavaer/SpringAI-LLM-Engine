package com.sfh.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName RegisterRequest
 * @Author sfh
 * @Version 1.0
 * @Description 注册请求实体
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
