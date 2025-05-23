package com.ryqg.jiaofu.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.ryqg.jiaofu.common.constants.SecurityConstants;
import com.ryqg.jiaofu.config.security.UserDetailsImpl;
import com.ryqg.jiaofu.domain.model.UserCredentials;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUtils {
    /**
     * 是否超级管理员
     * <p>
     * 超级管理员忽视任何权限判断
     */
    public static boolean isRoot() {
        Set<String> roles = getRoles();
        return roles.contains(SecurityConstants.ROOT_ROLE_CODE);
    }

    /**
     * 获取用户ID
     *
     * @return Long
     */
    public static String getPhone() {
        UserCredentials userCredentials = getUser().map(UserDetailsImpl::getUserCredentials).orElse(null);
        if (userCredentials != null) {
            return userCredentials.getPhone();
        }
        return "";
    }


    /**
     * 获取当前登录人信息
     *
     * @return Optional<SysUserDetails>
     */
    public static Optional<UserDetailsImpl> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetailsImpl) {
                return Optional.of((UserDetailsImpl) principal);
            }
        }
        return Optional.empty();
    }

    /**
     * 获取角色集合
     *
     * @return 角色集合
     */
    public static Set<String> getRoles() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getAuthorities)
                .filter(CollectionUtil::isNotEmpty)
                .stream()
                .flatMap(Collection::stream)
                .map(GrantedAuthority::getAuthority)
                // 筛选角色,authorities 中的角色都是以 ROLE_ 开头
                .filter(authority -> authority.startsWith(SecurityConstants.ROLE_PREFIX))
                .map(authority -> StrUtil.removePrefix(authority, SecurityConstants.ROLE_PREFIX))
                .collect(Collectors.toSet());
    }


    public static String getUserId() {
        UserCredentials userCredentials = getUser().map(UserDetailsImpl::getUserCredentials).orElse(null);
        if (userCredentials != null) {
            return userCredentials.getId();
        }
        return null;
    }

    public static String getTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
