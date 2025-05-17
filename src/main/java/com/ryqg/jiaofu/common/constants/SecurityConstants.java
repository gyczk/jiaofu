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

    /**
     * 超级管理员角色编码
     */
    String ROOT_ROLE_CODE = "ROOT";

    /**
     * 根节点ID
     */
    String ROOT_NODE_ID = "0";
}
