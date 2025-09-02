package com.sfh.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName AuthResponse
 * @Author sfh
 * @Version 1.0
 * @Description 认证响应实体
 **/
@Data
@ToString
@NoArgsConstructor
public class AuthResponse {

    private Boolean success;
    private String message;
    private String token;
    private User user;

    public AuthResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AuthResponse(Boolean success, String message, String token, User user) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.user = user;
    }
}
