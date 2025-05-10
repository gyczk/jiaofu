package com.ryqg.jiaofu.common.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryqg.jiaofu.business.common.BaseModel;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.domain.dto.BaseDTO;
import com.ryqg.jiaofu.domain.vo.BaseVO;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

public interface BaseConverter<Entity extends BaseModel,DTO extends BaseDTO,VO extends BaseVO> {
    Entity toEntity(DTO dto);

    BaseDTO toDTO(Entity entity);

    VO toVO(Entity entity);

    @Mappings({
            @Mapping(target = "pageNo", source = "current"),
            @Mapping(target = "pageSize", source = "size"),
            @Mapping(target = "total",  source = "total"),
            @Mapping(target = "data" , source = "records")
    })
    PageResult<VO> toPageResult(Page<Entity> page);


}
