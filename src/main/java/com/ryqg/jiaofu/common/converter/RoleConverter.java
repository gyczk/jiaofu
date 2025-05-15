package com.ryqg.jiaofu.common.converter;

import com.ryqg.jiaofu.domain.dto.RoleDTO;
import com.ryqg.jiaofu.domain.pojo.Role;
import com.ryqg.jiaofu.domain.vo.RoleVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleConverter extends BaseConverter<Role, RoleDTO, RoleVO>{
}
