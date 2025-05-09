package com.ryqg.jiaofu.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryqg.jiaofu.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User getUser(String phone);
}
