package com.ryqg.jiaofu.constants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.List;

public class AuthConstants {
    public static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/login",
            "/register",
            "/user/*"
    );

    /**
     * 认证请求头
     */
    public static final String AUTH_HEADER = "Authorization";

    /**
     * jwt的前缀
     */
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";

    /**
     * 用户名格式
     */
    public static final String USERNAME_MATCHING = "^[a-zA-Z0-9]+$";

    /**
     * 密码格式
     */
    public static final String PASSWORD_MATCHING = "^[a-zA-Z0-9_-]+$";

    /**
     * jwt过期时间
     */
    public static final Integer JWT_EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    /**
     * jwt签名算法
     */
    public final static SecureDigestAlgorithm<SecretKey, SecretKey> ALGORITHM = Jwts.SIG.HS256;
}
