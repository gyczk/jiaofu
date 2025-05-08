package com.ryqg.jiaofu.common.exception;

import com.ryqg.jiaofu.common.ResultCode;

/**
 * 密码错误异常
 */
public class PasswordErrorException extends BaseException {

    public PasswordErrorException(ResultCode resultCode) {
        super(resultCode);
    }
}