package com.ryqg.jiaofu.business.service;

import com.ryqg.jiaofu.business.common.IBaseService;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.domain.PageQuery.DictionaryPageQuery;
import com.ryqg.jiaofu.domain.dto.DictionaryDTO;
import com.ryqg.jiaofu.domain.vo.DictionaryVO;

public interface DictionaryService extends IBaseService<DictionaryDTO, DictionaryVO> {
    DictionaryVO getDictForm(String id);

    PageResult<DictionaryVO> pageQuery(DictionaryPageQuery dictionaryPageQuery);
}
