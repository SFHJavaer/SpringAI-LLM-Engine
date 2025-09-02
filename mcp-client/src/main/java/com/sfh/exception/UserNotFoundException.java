package com.sfh.exception;

/**
 * @ClassName UserNotFoundException
 * @Author sfh
 * @Version 1.0
 * @Description 用户不存在异常
 **/
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
