package com.ryqg.jiaofu.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryqg.jiaofu.domain.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> getMenusByRoleCodes(Set<String> roleCodes);
}
