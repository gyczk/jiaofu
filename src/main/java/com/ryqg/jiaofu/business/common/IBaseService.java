package com.ryqg.jiaofu.business.common;

import com.ryqg.jiaofu.domain.dto.BaseDTO;
import com.ryqg.jiaofu.domain.vo.BaseVO;

public interface IBaseService<DTO extends BaseDTO,VO extends BaseVO> {
    VO findById(String id);

    void save(DTO dto);

    void update(DTO dto);

    void delete(String ids);
}
