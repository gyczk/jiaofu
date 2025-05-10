package com.ryqg.jiaofu.business.common;

import cn.hutool.db.Page;
import com.ryqg.jiaofu.domain.dto.BaseDTO;
import com.ryqg.jiaofu.domain.vo.BaseVO;

public interface IBaseService<DTO extends BaseDTO,VO extends BaseVO> {
    VO findById(String id);

    int save(DTO dto);

    int update(DTO dto);

    int delete(String id);

    PageResult<VO> pageQuery(Page pageParam, DTO dto);
}
