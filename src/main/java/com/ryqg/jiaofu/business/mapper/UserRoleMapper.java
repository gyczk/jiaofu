package com.ryqg.jiaofu.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryqg.jiaofu.domain.pojo.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    void batchInsert(@Param("userId") String userId, @Param("roleIds") List<String> roleIds);

    List<String> listRoleIdsByUserId(@Param("userId") String userId);
}
