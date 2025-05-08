package com.ryqg.jiaofu.business.service.impl;

import com.ryqg.jiaofu.business.mapper.UserMapper;
import com.ryqg.jiaofu.pojo.User;
import com.ryqg.jiaofu.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userDao;

    public User getUserInfo(String phone){
        return userDao.getUser(phone);
    }

    @Override
    public int save(User user) {
        user.setUserId(UUID.randomUUID().toString());
        return userDao.save(user);
    }

}
