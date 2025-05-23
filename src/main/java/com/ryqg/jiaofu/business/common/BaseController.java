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

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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
        baseService.save(dto);
        return Result.success();
    }

    // 更新
    @PutMapping
    public Result<Void> update(@RequestBody DTO dto) {
        baseService.update(dto);
        return Result.success();
    }

    // 删除
    @DeleteMapping("/{ids}")
    public Result<Void> delete(@PathVariable String ids) {
        baseService.delete(ids);
        return Result.success();
    }
}
