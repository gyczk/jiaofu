package com.ryqg.jiaofu.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryqg.jiaofu.domain.pojo.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    List<String> listMenuIdsByRoleId(@Param("roleId") String roleId);
}
