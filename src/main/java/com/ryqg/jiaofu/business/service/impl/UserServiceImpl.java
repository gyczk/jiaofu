package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.sql.Direction;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.business.common.ServiceImpl;
import com.ryqg.jiaofu.business.mapper.UserMapper;
import com.ryqg.jiaofu.business.service.UserRoleService;
import com.ryqg.jiaofu.business.service.UserService;
import com.ryqg.jiaofu.common.converter.UserConverter;
import com.ryqg.jiaofu.domain.PageQuery.UserPageQuery;
import com.ryqg.jiaofu.domain.dto.UserDTO;
import com.ryqg.jiaofu.domain.pojo.User;
import com.ryqg.jiaofu.domain.vo.CurrentUserVO;
import com.ryqg.jiaofu.domain.vo.UserVO;
import com.ryqg.jiaofu.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public PageResult<UserVO> pageQuery(UserPageQuery userPageQuery) {
        Page<User> page = Page.of(userPageQuery.getPageNumber(), userPageQuery.getPageSize());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<User> lambda = queryWrapper.lambda();
        if (ArrayUtil.isNotEmpty(userPageQuery.getPhone())){
            lambda.like(User::getPhone, userPageQuery.getPhone());
        }
        if (ObjectUtil.isNotNull(userPageQuery.getStatus())){
            lambda.eq(User::getStatus, userPageQuery.getStatus());
        }
        LocalDateTime[] timeRangeArray = userPageQuery.getCreateTime();
        if (ArrayUtil.isNotEmpty(timeRangeArray) && timeRangeArray.length >= 1) {
            lambda.ge( User::getCreateTime,userPageQuery.getCreateTime()[0]);
        }
        if (ArrayUtil.isNotEmpty(timeRangeArray) && timeRangeArray.length >= 2) {
            lambda.le(User::getCreateTime, userPageQuery.getCreateTime()[1].toLocalDate().atTime(LocalTime.MAX));
        }
        if (ArrayUtil.isNotEmpty(userPageQuery.getOrders())) {
            Arrays.stream(userPageQuery.getOrders()).forEach(item -> {
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
