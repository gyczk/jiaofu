package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryqg.jiaofu.business.mapper.UserRoleMapper;
import com.ryqg.jiaofu.business.service.UserRoleService;
import com.ryqg.jiaofu.domain.pojo.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleMapper userRoleMapper;


    @Override
    public void saveUserRoles(String userId, List<String> roleIds) {
        // 先删除
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRole::getUserId, userId);
        userRoleMapper.delete(queryWrapper);
        if (CollectionUtil.isNotEmpty(roleIds)) {
            userRoleMapper.batchInsert(userId,roleIds);
        }
    }

    @Override
    public boolean hasAssignedUsers(String roleId) {
        return false;
    }

    @Override
    public void deleteByUserIds(String userIds) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(UserRole::getUserId, Arrays.stream(userIds.split(",")).collect(Collectors.toList()));
        userRoleMapper.delete(queryWrapper);
    }

    @Override
    public void deleteByRoleIds(String roleIds) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserRole::getRoleId,Arrays.stream(roleIds.split(",")).collect(Collectors.toList()));
        userRoleMapper.delete(queryWrapper);
    }


}
