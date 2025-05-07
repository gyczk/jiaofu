package com.ryqg.jiaofu.exception;

import com.ryqg.jiaofu.config.ResultCode;
import lombok.Getter;

/**
 * 用户注册失败
 */
@Getter
public class AccountRegisterFailException extends BaseException{

    public AccountRegisterFailException(ResultCode resultCode) {
        super(resultCode);
    }

}
