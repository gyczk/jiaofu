package com.ryqg.jiaofu.exception;


import com.ryqg.jiaofu.config.ResultCode;
import lombok.Getter;

/**
 * 账户锁定异常
 */
@Getter
public class AccountForbiddenException extends BaseException {

    public AccountForbiddenException(ResultCode resultCode) {
        super(resultCode);
    }

}
