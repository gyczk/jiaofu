package com.ryqg.jiaofu.business.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.annotation.Resource;

public abstract class ServiceImpl<M extends BaseMapper<T>, T extends BaseModel> implements IBaseService<T>{

    @Resource
    protected M baseMapper;

    @Override
    public T findById(String id) {
        return baseMapper.selectById(id);
    }

    @Override
    public int save(T t) {
        t.initNew();
        return baseMapper.insert(t);
    }

    @Override
    public int update(T t) {
        return baseMapper.updateById(t);
    }

    @Override
    public int delete(String id) {
        return baseMapper.deleteById(id);
    }
}
