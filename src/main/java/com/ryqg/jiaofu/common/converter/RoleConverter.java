package com.ryqg.jiaofu.common.converter;

import com.ryqg.jiaofu.domain.dto.RoleDTO;
import com.ryqg.jiaofu.domain.model.Option;
import com.ryqg.jiaofu.domain.pojo.Role;
import com.ryqg.jiaofu.domain.vo.RoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleConverter extends BaseConverter<Role, RoleDTO, RoleVO>{
    @Mappings({
     @Mapping(target = "value", source = "id"),
     @Mapping(target = "label", source = "name")
    })
    Option<String> toOption(Role role);

    List<Option<String>> toOptions(List<Role> roles);
}
