package com.ryqg.jiaofu.service.impl;

import com.ryqg.jiaofu.config.property.SecurityProperties;
import com.ryqg.jiaofu.mapper.UserMapper;
import com.ryqg.jiaofu.pojo.User;
import com.ryqg.jiaofu.service.UserService;
import com.ryqg.jiaofu.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userDao;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private  AuthenticationManager authenticationManager;

    @Autowired
    private RedisUtil redisUtil;

    public User getUserInfo(String phone){
        return userDao.getUser(phone);
    }

    @Override
    public int save(User user) {
        user.setUserId(UUID.randomUUID().toString());
        return userDao.save(user);
    }


}
