package com.ryqg.jiaofu.mapper;

import com.ryqg.jiaofu.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.userdetails.UserDetailsService;

@Mapper
public interface UserMapper {

    User getUser(String phone);

    int save(User user);
}
