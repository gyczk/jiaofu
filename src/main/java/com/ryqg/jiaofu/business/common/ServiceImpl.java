package com.ryqg.jiaofu.business.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryqg.jiaofu.common.converter.BaseConverter;
import com.ryqg.jiaofu.domain.dto.BaseDTO;
import com.ryqg.jiaofu.domain.vo.BaseVO;
import jakarta.annotation.Resource;

public abstract class ServiceImpl<M extends BaseMapper<Entity>, C extends BaseConverter<Entity,DTO,VO>,Entity
        extends BaseModel,DTO extends BaseDTO,VO extends BaseVO> implements IBaseService<DTO,VO>{

    @Resource
    protected M baseMapper;

    @Resource
    protected C baseConverter;


    @Override
    public VO findById(String id) {
        Entity entity = baseMapper.selectById(id);
        return baseConverter.toVO(entity);
    }

    @Override
    public int save(DTO dto) {
        Entity entity = baseConverter.toEntity(dto);
        entity.initNew();
        return baseMapper.insert(entity);
    }

    @Override
    public int update(DTO dto) {
        Entity entity = baseConverter.toEntity(dto);
        return baseMapper.updateById(entity);
    }

    @Override
    public int delete(String id) {
        return baseMapper.deleteById(id);
    }
}
