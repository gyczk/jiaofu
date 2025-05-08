package com.ryqg.jiaofu.common.exception;

import com.ryqg.jiaofu.common.ResultCode;

/**
 * 查询数据为空异常
 */
public class DataNotFoundException extends BaseException {

    public DataNotFoundException(ResultCode resultCode) {
        super(resultCode);
    }

}