package com.ryqg.jiaofu.common.exception;

import com.ryqg.jiaofu.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 捕获所有异常
     */
    @ExceptionHandler(Exception.class)
    public <T> Result<T> handleException(Exception ex) throws Exception {
        // 将 Spring Security 异常继续抛出，以便交给自定义处理器处理
        if (ex instanceof AccessDeniedException
                || ex instanceof AuthenticationException) {
            throw ex;
        }
        log.error("全局异常信息", ex);
        return Result.failed(ex.getLocalizedMessage());
    }

    /**
     * 捕获业务异常
     */
    @ExceptionHandler
    public <T> Result<T> exceptionHandler(BaseException ex){
        log.error("业务异常信息：{}", ex.getResultCode().getMsg());
        return Result.failed(ex.getResultCode());
    }


}
