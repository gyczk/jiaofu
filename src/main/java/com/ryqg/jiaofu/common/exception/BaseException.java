package com.ryqg.jiaofu.common.exception;

import com.ryqg.jiaofu.common.ResultCode;
import lombok.Getter;

/**
 * 基础业务异常类，所有自定义异常的父类
 */
@Getter
public abstract class BaseException extends RuntimeException {
    private final ResultCode resultCode;

    public BaseException(ResultCode resultCode) {
        super(resultCode.getMsg()); // 设置异常的详细信息
        this.resultCode = resultCode;
    }
}
