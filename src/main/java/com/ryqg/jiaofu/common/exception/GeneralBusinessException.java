package com.ryqg.jiaofu.common.exception;

import com.ryqg.jiaofu.common.ResultCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 提交或访问频繁异常
 */
@Getter
@Setter
public class GeneralBusinessException extends BaseException{

    public GeneralBusinessException(ResultCode resultCode) {
        super(resultCode);
    }

}
