package com.sfh.exception;

/**
 * @ClassName UserExistsException
 * @Author sfh
 * @Version 1.0
 * @Description 用户已存在异常
 **/
public class UserExistsException extends RuntimeException {

    public UserExistsException(String message) {
        super(message);
    }

    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
