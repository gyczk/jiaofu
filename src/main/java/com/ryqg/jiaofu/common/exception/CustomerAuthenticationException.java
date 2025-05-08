package com.ryqg.jiaofu.common.exception;

import com.ryqg.jiaofu.common.ResultCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomerAuthenticationException extends AuthenticationException {
    private final ResultCode resultCode;

    public CustomerAuthenticationException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }
}