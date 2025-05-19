package com.ryqg.jiaofu.common.converter;

import com.ryqg.jiaofu.domain.dto.UserDTO;
import com.ryqg.jiaofu.domain.dto.UserRegisterDTO;
import com.ryqg.jiaofu.domain.pojo.User;
import com.ryqg.jiaofu.domain.vo.CurrentUserVO;
import com.ryqg.jiaofu.domain.vo.UserVO;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserConverter extends BaseConverter<User, UserDTO, UserVO>{
    @Mapping(target = "password", qualifiedByName = "encryptPassword")
    User toEntity(UserRegisterDTO userRegisterDTO,@Context PasswordEncoder passwordEncode);

    CurrentUserVO toCurrentUserVO(User user);

    @Named("encryptPassword")
    default String encryptPassword(String password, @Context PasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }
}
