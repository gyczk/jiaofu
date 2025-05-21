package com.ryqg.jiaofu.business.common;

import com.ryqg.jiaofu.common.Result;
import com.ryqg.jiaofu.common.ResultCode;
import com.ryqg.jiaofu.domain.dto.BaseDTO;
import com.ryqg.jiaofu.domain.vo.BaseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
public abstract class BaseController<M extends IBaseService<DTO, VO>,
        DTO extends BaseDTO,
        VO extends BaseVO> {

    @Autowired
    protected M baseService;


    // 根据ID查询
    @GetMapping("/{id}")
    public Result<VO> getById(@PathVariable String id) {
        VO vo = baseService.findById(id);
        if (vo == null) {
            return Result.failed(ResultCode.DATA_NOT_EXIST);
        }
        return Result.success(vo);
    }

    // 创建
    @PostMapping
    public Result<Void> create(@RequestBody DTO dto) {
        int flag = baseService.save(dto);
        if (flag != 1) {
            return Result.failed(ResultCode.DATA_INSERT_ERROR);
        }
        return Result.success();
    }

    // 更新
    @PutMapping
    public Result<Void> update(@RequestBody DTO dto) {
        int flag = baseService.update(dto);
        if (flag != 1) {
            return Result.failed(ResultCode.DATA_UPDATE_ERROR);
        }
        return Result.success();
    }

    // 删除
    @DeleteMapping("/{ids}")
    public Result<Void> delete(@PathVariable String ids) {
        int flag = baseService.delete(ids);
        if (flag <= 0) {
            return Result.failed(ResultCode.DATA_DELETE_ERROR);
        }
        return Result.success();
    }
}
