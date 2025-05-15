package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.db.sql.Direction;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.business.common.ServiceImpl;
import com.ryqg.jiaofu.business.mapper.UserMapper;
import com.ryqg.jiaofu.business.service.UserService;
import com.ryqg.jiaofu.common.converter.UserConverter;
import com.ryqg.jiaofu.config.security.UserDetailsImpl;
import com.ryqg.jiaofu.domain.dto.UserDTO;
import com.ryqg.jiaofu.domain.pojo.User;
import com.ryqg.jiaofu.domain.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserConverter,User, UserDTO, UserVO> implements UserService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public int save(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return super.save(userDTO);
    }

    @Override
    public PageResult<UserVO> pageQuery(cn.hutool.db.Page pageParam, UserDTO dto) {
        Page<User> page = Page.of(pageParam.getPageNumber(), pageParam.getPageSize());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (dto != null) {
            queryWrapper.lambda().like(StringUtils.isNotBlank(dto.getUserName()), User::getUserName, dto.getUserName())
                    .eq(StringUtils.isNotBlank(dto.getPhone()), User::getPhone, dto.getPhone());
            }
        Arrays.stream(pageParam.getOrders()).forEach(item -> {
                    queryWrapper.orderBy(true, Direction.ASC.equals(item.getDirection()), item.getField());
        });
        page = baseMapper.selectPage(page,queryWrapper);
        return baseConverter.toPageResult(page);
    }

    @Override
    public UserVO getMe() {
        UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String phone = userDetails.getUser().getPhone();
        User user = baseMapper.getUser(phone);
        return baseConverter.toVO(user);
    }
}
