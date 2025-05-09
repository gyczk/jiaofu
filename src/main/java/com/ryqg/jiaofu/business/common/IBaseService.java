package com.ryqg.jiaofu.business.common;

public interface IBaseService<T extends BaseModel> {
    T findById(String id);
    int save(T t);
    int update(T t);
    int delete(String id);
}
