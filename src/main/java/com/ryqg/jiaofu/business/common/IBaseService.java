package com.ryqg.jiaofu.business.common;

import com.ryqg.jiaofu.domain.dto.BaseDTO;
import com.ryqg.jiaofu.domain.vo.BaseVO;

public interface IBaseService<DTO extends BaseDTO,VO extends BaseVO> {
    VO findById(String id);
    int save(DTO dto);
    int update(DTO dto);
    int delete(String id);
}
