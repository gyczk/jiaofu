package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserConverter, User, UserDTO, UserVO> implements UserService {
    private final PermissionCacheService permissionCacheService;

    private final UserRoleService userRoleService;

    @Override
    public void update(UserDTO userDTO) {
        List<String> roleIds = userDTO.getRoleIds();
        userRoleService.saveUserRoles(userDTO.getId(), roleIds);
        super.update(userDTO);
    }

    @Override
    public PageResult<UserVO> pageQuery(UserPageQuery userPageQuery) {
        Page<User> page = Page.of(userPageQuery.getPageNumber(), userPageQuery.getPageSize());

        boolean isRoot = SecurityUtils.isRoot();
        userPageQuery.setIsRoot(isRoot);
        Page<User> userPage = baseMapper.pageQuery(page,userPageQuery);
        return baseConverter.toPageResult(userPage);
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

    @Transactional
    @Override
    public void delete(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "无删除的数据");
        userRoleService.deleteByUserIds(ids);
        baseMapper.deleteBatchIds(Arrays.stream(ids.split(",")).collect(Collectors.toList()));
    }
}
