package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ryqg.jiaofu.business.mapper.RoleMenuMapper;
import com.ryqg.jiaofu.business.service.RoleMenuService;
import com.ryqg.jiaofu.domain.pojo.RoleMenu;
import com.ryqg.jiaofu.domain.vo.RolePermsVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleMenuServiceImpl implements RoleMenuService {
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<String> getMenuIds(String roleId) {
        return roleMenuMapper.listMenuIdsByRoleId(roleId);
    }

    @Override
    public void deleteByRoleIds(String roleIds) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleMenu::getRoleId, Arrays.stream(roleIds.split(",")).collect(Collectors.toList()));
        roleMenuMapper.delete(queryWrapper);
    }

    @Override
    public void saveRoleMenus(String roleId, List<String> menuIds) {
        if (CollectionUtil.isNotEmpty(menuIds)) {
            roleMenuMapper.batchInsert(roleId,menuIds);
        }
    }

    @Override
    public List<RolePermsVO> getRolePermsList(String roleId) {
        return roleMenuMapper.getRolePermsList(roleId);
    }
}
