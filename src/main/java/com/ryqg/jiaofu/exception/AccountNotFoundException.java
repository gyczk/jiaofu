package com.ryqg.jiaofu.exception;


import com.ryqg.jiaofu.config.ResultCode;
import lombok.Getter;

/**
 * 账户不存在异常
 */
@Getter
public class AccountNotFoundException extends BaseException {

    public AccountNotFoundException(ResultCode resultCode) {
        super(resultCode);
    }

}