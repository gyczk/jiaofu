package com.ryqg.jiaofu.common.exception;

import com.ryqg.jiaofu.common.ResultCode;

public class InvalidTokenException extends BaseException{
    public InvalidTokenException(ResultCode resultCode) {
        super(resultCode);
    }
}
