package com.ryqg.jiaofu.common.converter;

import com.ryqg.jiaofu.domain.dto.MenuDTO;
import com.ryqg.jiaofu.domain.pojo.Menu;
import com.ryqg.jiaofu.domain.vo.MenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuConverter extends BaseConverter<Menu, MenuDTO, MenuVO>{
    @Override
    @Mapping(target = "params", ignore = true)
    MenuVO toVO(Menu menu);
}
