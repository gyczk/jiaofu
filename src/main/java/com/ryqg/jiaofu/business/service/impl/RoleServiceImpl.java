package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.sql.Direction;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.business.common.ServiceImpl;
import com.ryqg.jiaofu.business.mapper.RoleMapper;
import com.ryqg.jiaofu.business.service.RoleService;
import com.ryqg.jiaofu.common.constants.SecurityConstants;
import com.ryqg.jiaofu.common.converter.RoleConverter;
import com.ryqg.jiaofu.domain.PageQuery.RolePageQuery;
import com.ryqg.jiaofu.domain.dto.RoleDTO;
import com.ryqg.jiaofu.domain.model.Option;
import com.ryqg.jiaofu.domain.pojo.Role;
import com.ryqg.jiaofu.domain.vo.RoleVO;
import com.ryqg.jiaofu.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleConverter, Role, RoleDTO, RoleVO> implements RoleService {
    @Override
    public PageResult<RoleVO> pageQuery(RolePageQuery rolePageQuery) {
        Page<Role> page = Page.of(rolePageQuery.getPageNumber(), rolePageQuery.getPageSize());
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<Role> lambda = queryWrapper.lambda();
        if (ArrayUtil.isNotEmpty(rolePageQuery.getName())){
            lambda.like(StringUtils.isNotBlank(rolePageQuery.getName()), Role::getName, rolePageQuery.getName());
            }
        if (ArrayUtil.isNotEmpty(rolePageQuery.getOrders())) {
            Arrays.stream(rolePageQuery.getOrders()).forEach(item -> {
                queryWrapper.orderBy(true,Direction.ASC.equals(item.getDirection()), item.getField());
            });
        } else {
            lambda.orderByAsc(Role::getSort);
        }
        page = baseMapper.selectPage(page,queryWrapper);
        return baseConverter.toPageResult(page);
    }

    @Override
    public RoleVO getRoleForm(String roleId) {
        return this.findById(roleId);
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
}
