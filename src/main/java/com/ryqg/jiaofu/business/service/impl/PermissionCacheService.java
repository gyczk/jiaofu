package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ryqg.jiaofu.business.service.RoleMenuService;
import com.ryqg.jiaofu.common.constants.RedisConstants;
import com.ryqg.jiaofu.domain.vo.RolePermsVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final RoleMenuService roleMenuService;

    @PostConstruct
    public void initRolePermsCache() {
        log.info("初始化权限缓存... ");
        refreshPermissions();
    }

    public Set<String> getPermissions(Set<String> roleCodes) {
        if (CollectionUtil.isEmpty(roleCodes)) {
            return Collections.emptySet();
        }

        Set<String> perms = new HashSet<>();
        Collection<String> roleCodesAsObjects = new ArrayList<>(roleCodes);
        HashOperations<String, String, Set<String>> hashOperations = redisTemplate.opsForHash();

        List<Set<String>> rolePermsList = hashOperations.multiGet(RedisConstants.System.ROLE_PERMS, roleCodesAsObjects);
        for (Set<String> rolePerms : rolePermsList) {
            if (CollectionUtil.isNotEmpty(rolePerms)) {
                perms.addAll(rolePerms);
            }
        }
        return perms;
    }
    // 刷新权限
    public void refreshPermissions(){
        redisTemplate.opsForHash().delete(RedisConstants.System.ROLE_PERMS,"*");
        refresh(null);
    }

    // 刷新所有权限
    public void refreshPermissions(String roleCode){
        redisTemplate.opsForHash().delete(RedisConstants.System.ROLE_PERMS,roleCode);
        refresh(roleCode);
    }

    public void refreshPermsCache(String oldRoleCode, String newRoleCode) {
        // 清理旧角色权限缓存
        redisTemplate.opsForHash().delete(RedisConstants.System.ROLE_PERMS, oldRoleCode);
        refresh(newRoleCode);
    }

    private void refresh(String roleCode){
        List<RolePermsVO> list = this.roleMenuService.getRolePermsList(roleCode);
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(item -> {
                String code = item.getRoleCode();
                Set<String> perms = item.getPerms();
                if (CollectionUtil.isNotEmpty(perms)) {
                    redisTemplate.opsForHash().put(RedisConstants.System.ROLE_PERMS, code, perms);
                }
            });
        }
    }
}
