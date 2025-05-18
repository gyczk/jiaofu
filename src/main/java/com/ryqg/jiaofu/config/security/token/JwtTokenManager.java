package com.ryqg.jiaofu.config.security.token;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ryqg.jiaofu.common.ResultCode;
import com.ryqg.jiaofu.common.constants.RedisConstants;
import com.ryqg.jiaofu.common.constants.SecurityConstants;
import com.ryqg.jiaofu.common.exception.InvalidTokenException;
import com.ryqg.jiaofu.config.property.SecurityProperties;
import com.ryqg.jiaofu.config.security.UserDetailsImpl;
import com.ryqg.jiaofu.domain.pojo.UserCredentials;
import com.ryqg.jiaofu.domain.vo.TokenUserVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class JwtTokenManager implements TokenManager {
    private final SecurityProperties securityProperties;

    private final RedisTemplate<String, Object> redisTemplate;

    private final byte[] secretKey;

    private final Integer accessTokenLiveTime;

    private final Integer refreshTokenLiveTime;

    public JwtTokenManager(SecurityProperties securityProperties, RedisTemplate<String, Object> redisTemplate) {
        this.securityProperties = securityProperties;
        this.redisTemplate = redisTemplate;
        this.secretKey = securityProperties.getSession().getJwt().getSecretKey().getBytes();
        this.accessTokenLiveTime = securityProperties.getSession().getAccessTokenTimeToLive();
        this.refreshTokenLiveTime = securityProperties.getSession().getRefreshTokenTimeToLive();
    }

    @Override
    public AuthenticationToken generateToken(Authentication authentication) {
        TokenUserVO tokenUserVO = buildTokenUserVo(authentication);
        String accessToken = generateToken(authentication, accessTokenLiveTime);
        String refreshToken = generateToken(authentication, refreshTokenLiveTime);

        // token 存储到 redis
        storeTokenInRedis(accessToken, refreshToken, tokenUserVO);

        // 单设备登录控制
        handleSingleDeviceLogin(tokenUserVO.getId(), accessToken, refreshToken);

        return AuthenticationToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenLiveTime)
                .build();
    }

    private TokenUserVO buildTokenUserVo(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return TokenUserVO.builder().userName(userDetails.getUsername())
                .phone(userDetails.getUserCredentials().getPhone())
                .id(userDetails.getUserCredentials().getId())
                .roles(userDetails.getUserCredentials().getRoles())
                .build();
    }

    private void handleSingleDeviceLogin(String userId, String token, String refreshToken) {
        Boolean allowMultiLogin = securityProperties.getSession().getRedisToken().getAllowMultiLogin();
        String tokenKey = formatRedisKey(RedisConstants.Auth.USER_ACCESS_TOKEN, userId);
        String refreshKey = formatRedisKey(RedisConstants.Auth.USER_REFRESH_TOKEN, userId);
        if (!allowMultiLogin) {
//            String oldAccessToken = (String) redisUtil.get(tokenKey);
            String oldAccessToken = (String) redisTemplate.opsForValue().get(tokenKey);
//            redisUtil.del(formatRedisKey(RedisConstants.Auth.ACCESS_TOKEN_USER, oldAccessToken));
            redisTemplate.delete(formatRedisKey(RedisConstants.Auth.ACCESS_TOKEN_USER, oldAccessToken));

//            String oldRefreshToken = (String) redisUtil.get(refreshKey);
            String oldRefreshToken = (String) redisTemplate.opsForValue().get(refreshKey);
//            redisUtil.del(formatRedisKey(RedisConstants.Auth.REFRESH_TOKEN_USER, oldRefreshToken));
            redisTemplate.delete(formatRedisKey(RedisConstants.Auth.REFRESH_TOKEN_USER, oldRefreshToken));

//            redisUtil.set(tokenKey, token, accessTokenLiveTime);
//            redisUtil.set(refreshKey, refreshToken, refreshTokenLiveTime);

            redisTemplate.opsForValue().set(tokenKey,token,accessTokenLiveTime, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(refreshKey,refreshToken,refreshTokenLiveTime, TimeUnit.SECONDS);
        }
    }


    private void storeTokenInRedis(String accessToken, String refreshToken, TokenUserVO tokenUserVO) {
        // token
//        redisUtil.set(formatRedisKey(RedisConstants.Auth.ACCESS_TOKEN_USER, accessToken), tokenUserVO, accessTokenLiveTime);
//        redisUtil.set(formatRedisKey(RedisConstants.Auth.REFRESH_TOKEN_USER, refreshToken), tokenUserVO, refreshTokenLiveTime);
        redisTemplate.opsForValue().set(formatRedisKey(RedisConstants.Auth.ACCESS_TOKEN_USER, accessToken), tokenUserVO, accessTokenLiveTime,TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(formatRedisKey(RedisConstants.Auth.REFRESH_TOKEN_USER, refreshToken), tokenUserVO, refreshTokenLiveTime);
    }

    private String generateToken(Authentication authentication, int ttl) {
        TokenUserVO tokenUserVO = buildTokenUserVo(authentication);
        Map<String, Object> payload = new HashMap<>();
        String loginUserString = JSON.toJSONString(tokenUserVO);
        payload.put(SecurityConstants.LOGIN_INFO, loginUserString);

        Date now = new Date();
        payload.put(JWTPayload.ISSUED_AT, now);

        // 设置过期时间 -1 表示永不过期
        if (ttl != -1) {
            Date expiresAt = DateUtil.offsetSecond(now, ttl);
            payload.put(JWTPayload.EXPIRES_AT, expiresAt);
        }
        payload.put(JWTPayload.SUBJECT, tokenUserVO.getUserName());
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
        TokenUserVO tokenUserVO = getUserFromToken(token);
        Set<SimpleGrantedAuthority> authorities = CollectionUtils.isNotEmpty(tokenUserVO.getRoles()) ? tokenUserVO.getRoles()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(Convert.toStr(authority)))
                .collect(Collectors.toSet()) : Collections.emptySet();
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setId(tokenUserVO.getId());
        userCredentials.setPhone(tokenUserVO.getPhone());
        userCredentials.setUserName(tokenUserVO.getUserName());
        userCredentials.setRoles(tokenUserVO.getRoles());
        UserDetailsImpl userDetails = new UserDetailsImpl(userCredentials);
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    private TokenUserVO getUserFromToken(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        JSONObject payloads = jwt.getPayloads().getJSONObject(SecurityConstants.LOGIN_INFO);
        try {
            return com.alibaba.fastjson.JSONObject.parseObject(payloads.toString(), TokenUserVO.class);
        } catch (com.alibaba.fastjson.JSONException e){
            throw new InvalidTokenException(ResultCode.ACCESS_TOKEN_VERIFICATION_FAILED);
        }
    }


    /**
     * 校验令牌
     *
     * @param token JWT Token
     * @return 是否有效
     */
    @Override
    public boolean validateToken(String token) {
        return true;
//        return redisTemplate.hasKey(formatRedisKey(RedisConstants.Auth.ACCESS_TOKEN_USER, token));
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
        return redisTemplate.hasKey(formatRedisKey(RedisConstants.Auth.REFRESH_TOKEN_USER, refreshToken));
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

        Authentication authentication = parseToken(refreshToken);
        String newAccessToken = generateToken(authentication, accessTokenLiveTime);

        TokenUserVO tokenUserVO = buildTokenUserVo(authentication);
        String userId = tokenUserVO.getId();
        // 删除旧token,单用户模式删,多用户不删有个问题：恶意登陆
        Boolean allowMultiLogin = securityProperties.getSession().getRedisToken().getAllowMultiLogin();
        if (!allowMultiLogin) {
//            String oldToken = (String) redisUtil.get(formatRedisKey(RedisConstants.Auth.USER_ACCESS_TOKEN, userId));
            String oldToken = (String) redisTemplate.opsForValue().get(formatRedisKey(RedisConstants.Auth.USER_ACCESS_TOKEN, userId));
//            redisUtil.del(formatRedisKey(RedisConstants.Auth.ACCESS_TOKEN_USER, oldToken));
//            redisUtil.set(formatRedisKey(RedisConstants.Auth.USER_ACCESS_TOKEN, userId), newAccessToken, accessTokenLiveTime);
            redisTemplate.delete(formatRedisKey(RedisConstants.Auth.ACCESS_TOKEN_USER, oldToken));
            redisTemplate.opsForValue().set(formatRedisKey(RedisConstants.Auth.USER_ACCESS_TOKEN, userId), newAccessToken, accessTokenLiveTime,TimeUnit.SECONDS);
        }
//        redisUtil.set(formatRedisKey(RedisConstants.Auth.ACCESS_TOKEN_USER, newAccessToken), tokenUserVO, accessTokenLiveTime);
        redisTemplate.opsForValue().set(formatRedisKey(RedisConstants.Auth.ACCESS_TOKEN_USER, newAccessToken), tokenUserVO, accessTokenLiveTime,TimeUnit.SECONDS);

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
     * @param params params
     * @return 格式化后的 Redis 键
     */
    private String formatRedisKey(String format, Object... params) {
        return StrUtil.format(format, params);
    }
}
