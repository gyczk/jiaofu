package com.ryqg.jiaofu.business.service;

import com.ryqg.jiaofu.business.common.IBaseService;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.domain.PageQuery.DictionaryPageQuery;
import com.ryqg.jiaofu.domain.dto.DictionaryItemDTO;
import com.ryqg.jiaofu.domain.model.Option;
import com.ryqg.jiaofu.domain.vo.DictionaryItemVO;

import java.util.List;

public interface DictionaryItemService extends IBaseService<DictionaryItemDTO, DictionaryItemVO> {
    List<Option<String>> getDictItems(String dictCode);

    PageResult<DictionaryItemVO> pageQuery(DictionaryPageQuery rolePageQuery);
}
