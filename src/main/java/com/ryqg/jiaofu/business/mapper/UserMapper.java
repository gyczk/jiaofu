package com.ryqg.jiaofu.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryqg.jiaofu.domain.pojo.User;
import com.ryqg.jiaofu.domain.pojo.UserCredentials;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User getUser(String phone);

    UserCredentials getAuthCredentialsByPhone(@Param("phone") String phone);
}
