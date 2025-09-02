package com.sfh.exception;

/**
 * @ClassName AuthException
 * @Author sfh
 * @Version 1.0
 * @Description 认证异常
 **/
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
