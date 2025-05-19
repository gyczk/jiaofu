package com.ryqg.jiaofu.common.constants;

public interface RedisConstants {
    /**
     * 认证模块
     */
    interface Auth {
        // 存储访问令牌对应的用户信息（accessToken -> User）
        String ACCESS_TOKEN_USER = "auth:token:access:{}";
        // 存储刷新令牌对应的用户信息（refreshToken -> User）
        String REFRESH_TOKEN_USER = "auth:token:refresh:{}";
        // 用户与访问令牌的映射（userId -> accessToken）
        String USER_ACCESS_TOKEN = "auth:user:access:{}";
        // 用户与刷新令牌的映射（userId -> refreshToken
        String USER_REFRESH_TOKEN = "auth:user:refresh:{}";
        // 黑名单 Token（用于退出登录或注销）
        String BLACKLIST_TOKEN = "auth:token:blacklist:{}";
    }

    /**
     * 系统模块
     */
    interface System {
        String CONFIG = "system:config";                 // 系统配置
        String ROLE_PERMS = "system:role:perms"; // 系统角色和权限映射
    }
}
