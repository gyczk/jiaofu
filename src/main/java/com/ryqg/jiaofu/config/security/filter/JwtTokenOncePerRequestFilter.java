package com.ryqg.jiaofu.config.security.filter;

import com.ryqg.jiaofu.common.BaseContext;
import com.ryqg.jiaofu.common.ResultCode;
import com.ryqg.jiaofu.common.exception.CustomerAuthenticationException;
import com.ryqg.jiaofu.config.property.SecurityProperties;
import com.ryqg.jiaofu.config.security.UserDetailsImpl;
import com.ryqg.jiaofu.config.security.handler.LoginFailureHandler;
import com.ryqg.jiaofu.config.security.token.TokenManager;
import com.ryqg.jiaofu.utils.TokenToUserDetailsUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenOncePerRequestFilter extends OncePerRequestFilter {
    private final SecurityProperties securityProperties; // JWT相关属性配置类

    private final TokenManager tokenManager; // Redis工具类

    private final LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 判断当前请求是否在白名单中
        String uri = request.getRequestURI();
        if (isWhitelisted(uri)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // 2. 校验token
            this.validateToken(request);
        } catch (AuthenticationException e) {
            loginFailureHandler.onAuthenticationFailure(request, response, e); // 处理登录失败的异常
            return;
        }
        filterChain.doFilter(request, response);
    }

    // 判断请求路径是否在白名单中
    private boolean isWhitelisted(String uri) {
        for (String pattern : securityProperties.getIgnoreUrls()) {
            if (pattern.endsWith("/**")) {
                // 处理通配符路径
                String basePattern = pattern.substring(0, pattern.length() - 3);
                if (uri.startsWith(basePattern)) {
                    return true;
                }
            } else if (pattern.equals(uri)) {
                // 精确匹配
                return true;
            }
        }
        return false;
    }

    // 校验token
    private void validateToken(HttpServletRequest request) {
        // 说明：登录了，再次请求其他需要认证的资源
        String token = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(token)) { // header没有token
            token = request.getParameter("Authorization");
        }
        if (ObjectUtils.isEmpty(token)) {
            throw new CustomerAuthenticationException(ResultCode.ACCESS_TOKEN_EMPTY);
        }
        // redis进行校验
        UserDetailsImpl userDetails = TokenToUserDetailsUtil.getUserDetails(token);
        String userId = userDetails.getUser().getId();
        if (!tokenManager.validateToken(token)) {
            throw new CustomerAuthenticationException(ResultCode.ACCESS_TOKEN_INVALID);
        }

        try {
            log.info("jwt校验:{}", token);
            log.info("当前用户id：{}", userId);
            BaseContext.setCurrentId(userId);
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            throw new CustomerAuthenticationException(ResultCode.ACCESS_TOKEN_VERIFICATION_FAILED);
        }
        // 把校验后的用户信息再次放入到SpringSecurity的上下文中
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities()); // 已认证的 Authentication 对象，包含用户的权限信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
