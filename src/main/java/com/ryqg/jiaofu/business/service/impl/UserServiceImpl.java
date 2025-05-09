package com.ryqg.jiaofu.business.service.impl;

import com.ryqg.jiaofu.business.common.ServiceImpl;
import com.ryqg.jiaofu.business.mapper.UserMapper;
import com.ryqg.jiaofu.business.service.UserService;
import com.ryqg.jiaofu.common.converter.UserConverterImpl;
import com.ryqg.jiaofu.domain.dto.UserDTO;
import com.ryqg.jiaofu.domain.pojo.User;
import com.ryqg.jiaofu.domain.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserConverterImpl,User, UserDTO, UserVO> implements UserService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public int save(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return super.save(userDTO);
    }
}
