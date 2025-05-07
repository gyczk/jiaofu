package com.ryqg.jiaofu.exception;

import com.ryqg.jiaofu.config.ResultCode;

/**
 * 查询数据为空异常
 */
public class DataNotFoundException extends BaseException {

    public DataNotFoundException(ResultCode resultCode) {
        super(resultCode);
    }

}