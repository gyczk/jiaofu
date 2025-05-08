package com.ryqg.jiaofu.config.security.handler;

import com.ryqg.jiaofu.common.ResultCode;
import com.ryqg.jiaofu.common.exception.CustomerAuthenticationException;
import com.ryqg.jiaofu.utils.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 发生这个行为，做响应处理，给一个响应的结果
        // 构建输出流对象
        ResultCode resultCode;
        if (exception instanceof AccountExpiredException) {
            resultCode = ResultCode.USER_ACCOUNT_EXPIRED;
        } else if (exception instanceof BadCredentialsException) {
            resultCode = ResultCode.USER_PASSWORD_ERROR;
        } else if (exception instanceof CredentialsExpiredException) {
            resultCode = ResultCode.ACCESS_TOKEN_INVALID;
        } else if (exception instanceof DisabledException || exception instanceof LockedException) {
            resultCode = ResultCode.USER_ACCOUNT_FROZEN;
        } else if (exception instanceof InternalAuthenticationServiceException) {
            resultCode = ResultCode.USER_NOT_EXIST;
        } else if (exception instanceof CustomerAuthenticationException customerAuthenticationException) {
            resultCode = customerAuthenticationException.getResultCode();
        } else {
            resultCode = ResultCode.USER_LOGIN_EXCEPTION;
        }
        ResponseUtils.writeErrMsg(response, resultCode);
    }
}