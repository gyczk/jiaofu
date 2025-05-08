package com.ryqg.jiaofu.config.security.token;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSON;
import com.ryqg.jiaofu.common.ResultCode;
import com.ryqg.jiaofu.config.property.SecurityProperties;
import com.ryqg.jiaofu.config.security.UserDetailsImpl;
import com.ryqg.jiaofu.common.constants.RedisConstants;
import com.ryqg.jiaofu.common.constants.SecurityConstants;
import com.ryqg.jiaofu.common.exception.InvalidTokenException;
import com.ryqg.jiaofu.utils.RedisUtil;
import com.ryqg.jiaofu.utils.TokenToUserDetailsUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenManager implements TokenManager {
    private final SecurityProperties securityProperties;

    private final RedisUtil redisUtil;

    private final byte[] secretKey;

    private final Integer accessTokenLiveTime;

    private final Integer refreshTokenLiveTime;

    public JwtTokenManager(SecurityProperties securityProperties, RedisUtil redisUtil) {
        this.securityProperties = securityProperties;
        this.redisUtil = redisUtil;
        this.secretKey = securityProperties.getSession().getJwt().getSecretKey().getBytes();
        this.accessTokenLiveTime = securityProperties.getSession().getAccessTokenTimeToLive();
        this.refreshTokenLiveTime = securityProperties.getSession().getRefreshTokenTimeToLive();
    }

    @Override
    public AuthenticationToken generateToken(Authentication authentication) {
        String accessToken = generateToken(authentication, accessTokenLiveTime);
        String refreshToken = generateToken(authentication, refreshTokenLiveTime);
        UserDetailsImpl userDetails = TokenToUserDetailsUtil.getUserDetails(accessToken);

        // token 存储到 redis
        storeTokenInRedis(accessToken, refreshToken, userDetails);

        // 单设备登录控制
        handleSingleDeviceLogin(userDetails.getUser().getUserId(), accessToken, refreshToken);

        return AuthenticationToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenLiveTime)
                .build();
    }

    private void handleSingleDeviceLogin(String userId, String token, String refreshToken) {
        Boolean allowMultiLogin = securityProperties.getSession().getRedisToken().getAllowMultiLogin();
        String tokenKey = formatRedisKey(userId, RedisConstants.Auth.USER_ACCESS_TOKEN);
        String refreshKey = formatRedisKey(userId, RedisConstants.Auth.USER_REFRESH_TOKEN);
        if (!allowMultiLogin) {
            String oldAccessToken = (String) redisUtil.get(tokenKey);
            redisUtil.del(formatRedisKey(oldAccessToken, RedisConstants.Auth.ACCESS_TOKEN_USER));

            String oldRefreshToken = (String) redisUtil.get(refreshKey);
            redisUtil.del(formatRedisKey(oldRefreshToken, RedisConstants.Auth.REFRESH_TOKEN_USER));

            redisUtil.set(tokenKey, token, accessTokenLiveTime);
            redisUtil.set(refreshKey, refreshToken, refreshTokenLiveTime);
        }
    }


    private void storeTokenInRedis(String accessToken, String refreshToken, UserDetailsImpl userDetails) {
        // token
        redisUtil.set(formatRedisKey(accessToken, RedisConstants.Auth.ACCESS_TOKEN_USER), userDetails, accessTokenLiveTime);
        redisUtil.set(formatRedisKey(refreshToken, RedisConstants.Auth.REFRESH_TOKEN_USER), userDetails, refreshTokenLiveTime);
    }

    private String generateToken(Authentication authentication, int ttl) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Map<String, Object> payload = new HashMap<>();
        String loginEmpString = JSON.toJSONString(userDetails);
        payload.put(SecurityConstants.LOGIN_INFO, loginEmpString);

        Date now = new Date();
        payload.put(JWTPayload.ISSUED_AT, now);

        // 设置过期时间 -1 表示永不过期
        if (ttl != -1) {
            Date expiresAt = DateUtil.offsetSecond(now, ttl);
            payload.put(JWTPayload.EXPIRES_AT, expiresAt);
        }
        payload.put(JWTPayload.SUBJECT, authentication.getName());
        payload.put(JWTPayload.JWT_ID, IdUtil.simpleUUID());

        return JWTUtil.createToken(payload, secretKey);
    }

    /**
     * 解析令牌
     *
     * @param token JWT Token
     * @return Authentication 对象
     */
    @Override
    public Authentication parseToken(String token) {
        UserDetailsImpl userDetails = TokenToUserDetailsUtil.getUserDetails(token);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


    /**
     * 校验令牌
     *
     * @param token JWT Token
     * @return 是否有效
     */
    @Override
    public boolean validateToken(String token) {
        return redisUtil.hasKey(formatRedisKey(token, RedisConstants.Auth.ACCESS_TOKEN_USER));
        /*JWT jwt = JWTUtil.parseToken(token);
        // 检查 Token 是否有效(验签 + 是否过期)
        boolean isValid = jwt.setKey(secretKey).validate(0);

        *//*if (isValid) {
            // 检查 Token 是否已被加入黑名单(注销、修改密码等场景)
            JSONObject payloads = jwt.getPayloads();
            String jti = payloads.getStr(JWTPayload.JWT_ID);

            // 判断是否在黑名单中，如果在，则返回 false 标识Token无效
            if (Boolean.TRUE.equals(redisTemplate.hasKey(StrUtil.format(RedisConstants.Auth.BLACKLIST_TOKEN, jti)))) {
                return false;
            }
        }*/
    }


    @Override
    public boolean validateRefreshToken(String refreshToken) {
        return redisUtil.hasKey(formatRedisKey(refreshToken, RedisConstants.Auth.REFRESH_TOKEN_USER));
    }

    /**
     * 刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 令牌响应对象
     */
    @Override
    public AuthenticationToken refreshToken(String refreshToken) {

        boolean isValid = validateRefreshToken(refreshToken);
        if (!isValid) {
            throw new InvalidTokenException(ResultCode.REFRESH_TOKEN_INVALID);
        }
        UserDetailsImpl userDetails = TokenToUserDetailsUtil.getUserDetails(refreshToken);
        String userId = userDetails.getUser().getUserId();

        Authentication authentication = parseToken(refreshToken);
        String newAccessToken = generateToken(authentication, accessTokenLiveTime);

        // 删除旧token,单用户模式删,多用户不删有个问题：恶意登陆
        Boolean allowMultiLogin = securityProperties.getSession().getRedisToken().getAllowMultiLogin();
        if (!allowMultiLogin) {
            String oldToken = (String)redisUtil.get(formatRedisKey(userId, RedisConstants.Auth.USER_ACCESS_TOKEN));
            redisUtil.del(formatRedisKey(oldToken, RedisConstants.Auth.ACCESS_TOKEN_USER));
            redisUtil.set(formatRedisKey(userId, RedisConstants.Auth.USER_ACCESS_TOKEN), newAccessToken, accessTokenLiveTime);
        }
        redisUtil.set(formatRedisKey(newAccessToken, RedisConstants.Auth.ACCESS_TOKEN_USER), userDetails, accessTokenLiveTime);

        return AuthenticationToken.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenLiveTime)
                .build();
    }


    @Override
    public void invalidateToken(String token) {
        TokenManager.super.invalidateToken(token);
    }

    /**
     * 格式化访问令牌的 Redis 键
     *
     * @param content content
     * @return 格式化后的 Redis 键
     */
    private String formatRedisKey(String content, String format) {
        return StrUtil.format(format, content);
    }
}
