package com.ryqg.jiaofu.business.common;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryqg.jiaofu.common.converter.BaseConverter;
import com.ryqg.jiaofu.domain.dto.BaseDTO;
import com.ryqg.jiaofu.domain.vo.BaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class ServiceImpl<M extends BaseMapper<Entity>, C extends BaseConverter<Entity,DTO,VO>,Entity
        extends BaseModel,DTO extends BaseDTO,VO extends BaseVO> implements IBaseService<DTO,VO>{

    @Autowired
    protected M baseMapper;

    @Autowired
    protected C baseConverter;


    @Override
    public VO findById(String id) {
        Entity entity = baseMapper.selectById(id);
        return baseConverter.toVO(entity);
    }

    @Transactional
    @Override
    public void save(DTO dto) {
        Entity entity = baseConverter.toEntity(dto);
        baseMapper.insert(entity);
    }

    @Transactional
    @Override
    public void update(DTO dto) {
        Entity entity = baseConverter.toEntity(dto);
        baseMapper.updateById(entity);
    }

    @Transactional
    @Override
    public void delete(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "无删除的数据");
        baseMapper.deleteBatchIds(Arrays.stream(ids.split(",")).collect(Collectors.toList()));
    }

}
