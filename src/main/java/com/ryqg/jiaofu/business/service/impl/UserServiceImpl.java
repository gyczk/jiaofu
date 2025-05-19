package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.sql.Direction;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.business.common.ServiceImpl;
import com.ryqg.jiaofu.business.mapper.UserMapper;
import com.ryqg.jiaofu.business.service.UserRoleService;
import com.ryqg.jiaofu.business.service.UserService;
import com.ryqg.jiaofu.common.converter.UserConverter;
import com.ryqg.jiaofu.domain.dto.UserDTO;
import com.ryqg.jiaofu.domain.pojo.User;
import com.ryqg.jiaofu.domain.vo.CurrentUserVO;
import com.ryqg.jiaofu.domain.vo.UserVO;
import com.ryqg.jiaofu.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserConverter, User, UserDTO, UserVO> implements UserService {
    private final PasswordEncoder passwordEncoder;

    private final PermissionCacheService permissionCacheService;

    private final UserRoleService userRoleService;

    @Override
    public int update(UserDTO userDTO) {
        List<String> roleIds = userDTO.getRoleIds();
        userRoleService.saveUserRoles(userDTO.getId(), roleIds);
        return super.update(userDTO);
    }

    @Override
    public PageResult<UserVO> pageQuery(cn.hutool.db.Page pageParam, UserDTO dto) {
        Page<User> page = Page.of(pageParam.getPageNumber(), pageParam.getPageSize());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (dto != null) {
            queryWrapper.lambda().like(StringUtils.isNotBlank(dto.getUserName()), User::getUserName, dto.getUserName())
                    .eq(StringUtils.isNotBlank(dto.getPhone()), User::getPhone, dto.getPhone());
        }
        if (ArrayUtil.isNotEmpty(pageParam.getOrders())) {
            Arrays.stream(pageParam.getOrders()).forEach(item -> {
                queryWrapper.orderBy(true, Direction.ASC.equals(item.getDirection()), item.getField());
            });
        }
        page = baseMapper.selectPage(page, queryWrapper);
        return baseConverter.toPageResult(page);
    }

    @Override
    public CurrentUserVO getMe() {
        String phone = SecurityUtils.getPhone();
        User user = baseMapper.getUser(phone);
        CurrentUserVO currentUserVO = baseConverter.toCurrentUserVO(user);
        // 设置 roles
        Set<String> roles = SecurityUtils.getRoles();
        currentUserVO.setRoles(roles);

        //设置 perms,从缓存中获取
        Set<String> perms = permissionCacheService.getPermissions(roles);
        currentUserVO.setPerms(perms);

        return currentUserVO;
    }

    @Override
    public UserVO getUserForm(String userId) {
        return baseMapper.getUserForm(userId);
    }
}
