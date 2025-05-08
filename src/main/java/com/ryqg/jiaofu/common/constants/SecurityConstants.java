package com.ryqg.jiaofu.common.constants;

/**
 * 安全模块常量
 */
public interface SecurityConstants {
    /**
     * JWT Token 前缀
     */
    String BEARER_TOKEN_PREFIX  = "Bearer ";

    /**
     * 角色前缀，用于区分 authorities 角色和权限， ROLE_* 角色 、没有前缀的是权限
     */
    String ROLE_PREFIX = "ROLE_";

    String LOGIN_INFO = "LOGIN_INFO";
}
