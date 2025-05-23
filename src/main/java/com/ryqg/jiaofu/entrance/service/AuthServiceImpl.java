package com.ryqg.jiaofu.entrance.service;

import com.ryqg.jiaofu.business.mapper.UserMapper;
import com.ryqg.jiaofu.common.ResultCode;
import com.ryqg.jiaofu.common.converter.UserConverter;
import com.ryqg.jiaofu.common.exception.InvalidTokenException;
import com.ryqg.jiaofu.config.security.token.AuthenticationToken;
import com.ryqg.jiaofu.config.security.token.TokenManager;
import com.ryqg.jiaofu.domain.dto.UserRegisterDTO;
import com.ryqg.jiaofu.domain.pojo.User;
import com.ryqg.jiaofu.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final TokenManager tokenManager;

    private final UserMapper userMapper;

    private final UserConverter userConverter;

    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationToken login(String phone, String password) {
        // 创建未认证令牌
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phone, password);
        // 执行认证（认证中）
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return tokenManager.generateToken(authenticate);
    }

    @Override
    public AuthenticationToken refreshToken(String refreshToken) {
        // 验证刷新令牌
        boolean isValidate = tokenManager.validateRefreshToken(refreshToken);

        if (!isValidate) {
            throw new InvalidTokenException(ResultCode.REFRESH_TOKEN_INVALID);
        }
        // 刷新令牌有效，生成新的访问令牌
        return tokenManager.refreshToken(refreshToken);
    }

    @Override
    public int save(UserRegisterDTO userRegisterDTO) {
        User user = userConverter.toEntity(userRegisterDTO,passwordEncoder);
        return userMapper.insert(user);
    }

    @Override
    public void logout() {
        String token = SecurityUtils.getTokenFromRequest();
        tokenManager.invalidateToken(token);
        // 此处请求url 不拦截，不会有context
        SecurityContextHolder.clearContext();
    }
}
