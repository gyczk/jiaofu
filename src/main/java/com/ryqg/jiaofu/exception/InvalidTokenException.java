package com.ryqg.jiaofu.exception;

import com.ryqg.jiaofu.config.ResultCode;

public class InvalidTokenException extends BaseException{
    public InvalidTokenException(ResultCode resultCode) {
        super(resultCode);
    }
}
