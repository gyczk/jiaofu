package com.ryqg.jiaofu.common.converter;

import com.ryqg.jiaofu.domain.dto.UserDTO;
import com.ryqg.jiaofu.domain.pojo.User;
import com.ryqg.jiaofu.domain.vo.UserVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserConverter extends BaseConverter<User, UserDTO, UserVO>{
}
