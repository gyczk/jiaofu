package com.ryqg.jiaofu.exception;

import com.ryqg.jiaofu.config.ResultCode;

/**
 * 密码错误异常
 */
public class PasswordErrorException extends BaseException {

    public PasswordErrorException(ResultCode resultCode) {
        super(resultCode);
    }
}