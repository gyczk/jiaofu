package com.ryqg.jiaofu.common.exception;

import com.ryqg.jiaofu.common.Result;
import com.ryqg.jiaofu.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return Result.failed(ResultCode.SYSTEM_ERROR);
    }

    /**
     * 捕获业务异常
     */
    @ExceptionHandler
    public <T> Result<T> exceptionHandler(BaseException ex){
        log.error("业务异常信息：{}", ex.getResultCode().getMsg());
        return Result.failed(ex.getResultCode());
    }

    /**
     * 处理SQL异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public <T> Result<T> exceptionHandler(DuplicateKeyException ex){
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String regex = "Duplicate entry '([^']+)' for key";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(message);
            String msg =  ResultCode.USERNAME_ALREADY_EXISTS.getMsg();
            if (matcher.find()){
                msg = matcher.group(1) + ":" + msg;
            }
            return Result.failed(ResultCode.USERNAME_ALREADY_EXISTS,msg);
        }else {
            return Result.failed(ResultCode.SYSTEM_ERROR);
        }

    }

}
