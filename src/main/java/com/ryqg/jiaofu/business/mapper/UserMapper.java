package com.ryqg.jiaofu.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryqg.jiaofu.domain.PageQuery.UserPageQuery;
import com.ryqg.jiaofu.domain.pojo.User;
import com.ryqg.jiaofu.domain.model.UserCredentials;
import com.ryqg.jiaofu.domain.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User getUser(String phone);

    UserCredentials getAuthCredentialsByPhone(@Param("phone") String phone);

    UserVO getUserForm(@Param("userId") String userId);

    Page<User> pageQuery(Page<User> page,@Param("pageQuery") UserPageQuery pageQuery);
}
