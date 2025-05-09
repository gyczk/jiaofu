package com.ryqg.jiaofu.business.common;

import com.ryqg.jiaofu.common.Result;
import com.ryqg.jiaofu.common.ResultCode;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseController<M extends IBaseService<T>,T extends BaseModel> {
    @Resource
    protected M baseService;


    // 根据ID查询
    @GetMapping("/{id}")
    public Result<T> getById(@PathVariable String id) {
        T t = baseService.findById(id);
        if (t == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.success(t);
    }

    // 创建
    @PostMapping
    public Result<T> create(@RequestBody T t) {
        int flag = baseService.save(t);
        if (flag != 1) {
            return Result.failed(ResultCode.DATA_INSERT_ERROR);
        }
        return Result.success();
    }

    // 更新
    @PutMapping("/{id}")
    public Result<T> update(@RequestBody T t) {
        int flag = baseService.update(t);
        if (flag != 1) {
            return Result.failed(ResultCode.DATA_UPDATE_ERROR);
        }
        return Result.success();
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        int flag = baseService.delete(id);
        if (flag != 1) {
            return Result.failed(ResultCode.DATA_DELETE_ERROR);
        }
        return Result.success();
    }
}
