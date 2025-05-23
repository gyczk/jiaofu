package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.sql.Direction;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.business.common.ServiceImpl;
import com.ryqg.jiaofu.business.mapper.RoleMapper;
import com.ryqg.jiaofu.business.service.RoleMenuService;
import com.ryqg.jiaofu.business.service.RoleService;
import com.ryqg.jiaofu.business.service.UserRoleService;
import com.ryqg.jiaofu.common.constants.SecurityConstants;
import com.ryqg.jiaofu.common.converter.RoleConverter;
import com.ryqg.jiaofu.domain.PageQuery.RolePageQuery;
import com.ryqg.jiaofu.domain.dto.RoleDTO;
import com.ryqg.jiaofu.domain.model.Option;
import com.ryqg.jiaofu.domain.pojo.Role;
import com.ryqg.jiaofu.domain.vo.RoleVO;
import com.ryqg.jiaofu.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleConverter, Role, RoleDTO, RoleVO> implements RoleService {
    private final UserRoleService userRoleService;

    private final RoleMenuService roleMenuService;

    private final PermissionCacheService permissionCacheService;

    @Override
    public PageResult<RoleVO> pageQuery(RolePageQuery rolePageQuery) {
        Page<Role> page = Page.of(rolePageQuery.getPageNumber(), rolePageQuery.getPageSize());
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<Role> lambda = queryWrapper.lambda();
        if (ArrayUtil.isNotEmpty(rolePageQuery.getName())){
            lambda.like(StringUtils.isNotBlank(rolePageQuery.getName()), Role::getName, rolePageQuery.getName());
            }
        if (ArrayUtil.isNotEmpty(rolePageQuery.getOrders())) {
            Arrays.stream(rolePageQuery.getOrders()).forEach(
                    item -> queryWrapper.orderBy(true,Direction.ASC.equals(item.getDirection()), item.getField()));
        } else {
            lambda.orderByAsc(Role::getSort);
        }
        lambda.ne(!SecurityUtils.isRoot(),Role::getCode,SecurityConstants.ROOT_ROLE_CODE);
        page = baseMapper.selectPage(page,queryWrapper);
        return baseConverter.toPageResult(page);
    }

    @Override
    public RoleVO getRoleForm(String roleId) {
        return this.findById(roleId);
    }

    @Override
    public List<String> getRoleMenuIds(String roleId) {
        return roleMenuService.getMenuIds(roleId);
    }

    @Override
    public void assignMenusToRole(String roleId, List<String> menuIds) {
        Role role = baseMapper.selectById(roleId);
        if (ObjectUtil.isNull(role)) {
            throw new RuntimeException("角色不存在");
        }
        //先删除关联的菜单
        roleMenuService.deleteByRoleIds(roleId);
        // 再保存
        roleMenuService.saveRoleMenus(roleId,menuIds);
        // 刷新权限缓存
        permissionCacheService.refreshPermissions(role.getCode());
    }

    @Override
    public List<Option<String>> getRoleOptions() {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ne(!SecurityUtils.isRoot(),Role::getCode, SecurityConstants.ROOT_ROLE_CODE)
                .select(Role::getId, Role::getName)
                .orderByAsc(Role::getSort);
        List<Role> roles = baseMapper.selectList(queryWrapper);
        return baseConverter.toOptions(roles);
    }

    @Transactional
    @Override
    public void delete(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "无删除的数据");
        List<String> roleIdList = Arrays.stream(ids.split(",")).toList();
        // 刷新权限缓存
        for (String roleId : roleIdList) {
            Role role = baseMapper.selectById(roleId);
            if (ObjectUtil.isNotNull(role)) {
                permissionCacheService.refreshPermissions(role.getCode());
                userRoleService.deleteByRoleIds(role.getId());
                baseMapper.deleteById(role.getId());
            }

        }
    }

    @Override
    public void update(RoleDTO dto) {
        Role role = baseConverter.toEntity(dto);
        baseMapper.updateById(role);
        permissionCacheService.refreshPermissions(role.getCode());
    }
}
