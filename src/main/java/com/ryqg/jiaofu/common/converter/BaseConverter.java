package com.ryqg.jiaofu.common.converter;

import com.ryqg.jiaofu.business.common.BaseModel;
import com.ryqg.jiaofu.domain.dto.BaseDTO;
import com.ryqg.jiaofu.domain.dto.UserDTO;
import com.ryqg.jiaofu.domain.vo.BaseVO;

public interface BaseConverter<Entity extends BaseModel,DTO extends BaseDTO,VO extends BaseVO> {
    Entity toEntity(DTO dto);

    UserDTO toDTO(Entity entity);

    VO toVO(Entity entity);
}
