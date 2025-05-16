package com.ryqg.jiaofu.entrance.service;

import com.ryqg.jiaofu.config.security.token.AuthenticationToken;
import com.ryqg.jiaofu.domain.dto.UserRegisterDTO;

public interface AuthService {
    AuthenticationToken login(String phone, String password);

    AuthenticationToken refreshToken(String refreshToken);

    int save(UserRegisterDTO userRegisterDTO);
}
