package com.ryqg.jiaofu.business.service.impl;

import com.ryqg.jiaofu.business.mapper.RoleMenuMapper;
import com.ryqg.jiaofu.business.service.RoleMenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleMenuServiceImpl implements RoleMenuService {
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<String> getMenuIds(String roleId) {
        return roleMenuMapper.listMenuIdsByRoleId(roleId);
    }
}
