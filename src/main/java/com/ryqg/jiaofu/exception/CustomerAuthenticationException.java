package com.ryqg.jiaofu.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Jiang YinHui
 * @version 1.0
 * @description 自定义认证验证异常
 * @create 2024-10-23  16:42
 */
public class CustomerAuthenticationException extends AuthenticationException {

    public CustomerAuthenticationException(String msg) {
        super(msg);
    }
}