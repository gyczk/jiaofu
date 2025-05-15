package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.db.sql.Direction;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.business.common.ServiceImpl;
import com.ryqg.jiaofu.business.mapper.RoleMapper;
import com.ryqg.jiaofu.business.service.RoleService;
import com.ryqg.jiaofu.common.converter.RoleConverter;
import com.ryqg.jiaofu.domain.dto.RoleDTO;
import com.ryqg.jiaofu.domain.pojo.Role;
import com.ryqg.jiaofu.domain.vo.RoleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleConverter, Role, RoleDTO, RoleVO> implements RoleService {
    
    @Override
    public PageResult<RoleVO> pageQuery(cn.hutool.db.Page pageParam, RoleDTO dto) {
        Page<Role> page = Page.of(pageParam.getPageNumber(), pageParam.getPageSize());
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (dto != null) {
            queryWrapper.lambda().like(StringUtils.isNotBlank(dto.getName()), Role::getName, dto.getName());
            }
        Arrays.stream(pageParam.getOrders()).forEach(item -> {
                    queryWrapper.orderBy(true, Direction.ASC.equals(item.getDirection()), item.getField());
        });
        page = baseMapper.selectPage(page,queryWrapper);
        return baseConverter.toPageResult(page);
    }
}
