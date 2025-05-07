package com.ryqg.jiaofu.entrance.service;

import com.ryqg.jiaofu.config.security.AuthenticationToken;

public interface AuthService {
    AuthenticationToken login(String phone, String password);

    AuthenticationToken refreshToken(String refreshToken);
}
