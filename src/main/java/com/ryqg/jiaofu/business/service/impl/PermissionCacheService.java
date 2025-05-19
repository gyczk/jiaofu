package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ryqg.jiaofu.common.constants.RedisConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PermissionCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

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
}
