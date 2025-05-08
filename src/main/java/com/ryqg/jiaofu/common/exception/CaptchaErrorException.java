package com.ryqg.jiaofu.common.exception;

import com.ryqg.jiaofu.common.ResultCode;
import lombok.Getter;

/**
 * 验证码错误异常
 */
@Getter
public class CaptchaErrorException extends BaseException {

    public CaptchaErrorException(ResultCode resultCode) {
        super(resultCode);
    }

}
