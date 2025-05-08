package com.ryqg.jiaofu.common.exception;

import com.ryqg.jiaofu.common.ResultCode;
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
