package com.ryqg.jiaofu.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryqg.jiaofu.domain.pojo.RoleMenu;
import com.ryqg.jiaofu.domain.vo.RolePermsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    List<String> listMenuIdsByRoleId(@Param("roleId") String roleId);

    void batchInsert(@Param("roleId")String roleId, @Param("menuIds")List<String> menuIds);

    List<RolePermsVO> getRolePermsList(@Param("roleCode")String roleCode);
}
