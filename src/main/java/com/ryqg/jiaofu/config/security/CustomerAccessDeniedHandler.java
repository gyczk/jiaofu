package com.ryqg.jiaofu.config.security;

import com.ryqg.jiaofu.config.ResultCode;
import com.ryqg.jiaofu.utils.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.error("权限不足，URI：{}，异常：{}", request.getRequestURI(), accessDeniedException.getMessage());
        // 发生这个行为，做响应处理，给一个响应的结果
        ResponseUtils.writeErrMsg(response, ResultCode.ACCESS_UNAUTHORIZED);

    }
}