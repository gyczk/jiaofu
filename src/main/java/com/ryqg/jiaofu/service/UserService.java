package com.ryqg.jiaofu.service;

import com.ryqg.jiaofu.pojo.LoginDTO;
import com.ryqg.jiaofu.pojo.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User getUserInfo(String phone);

    int save(User user);

}
