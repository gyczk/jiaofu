package com.ryqg.jiaofu.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.business.common.ServiceImpl;
import com.ryqg.jiaofu.business.mapper.DictionaryItemMapper;
import com.ryqg.jiaofu.business.service.DictionaryItemService;
import com.ryqg.jiaofu.common.converter.DictionaryItemConverter;
import com.ryqg.jiaofu.domain.PageQuery.DictionaryPageQuery;
import com.ryqg.jiaofu.domain.dto.DictionaryItemDTO;
import com.ryqg.jiaofu.domain.model.Option;
import com.ryqg.jiaofu.domain.pojo.DictionaryItem;
import com.ryqg.jiaofu.domain.vo.DictionaryItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DictionaryItemServiceImpl extends ServiceImpl<DictionaryItemMapper, DictionaryItemConverter, DictionaryItem, DictionaryItemDTO, DictionaryItemVO> implements DictionaryItemService {
    @Override
    public List<Option<String>> getDictItems(String dictCode) {
        QueryWrapper<DictionaryItem> queryWrapper = new QueryWrapper<DictionaryItem>();
        queryWrapper.lambda().eq(DictionaryItem::getDictCode, dictCode)
                .eq(DictionaryItem::getStatus,1)
                .orderByAsc(DictionaryItem::getSort);
        List<DictionaryItem> dictionaryItems = baseMapper.selectList(queryWrapper);
        return baseConverter.toOptions(dictionaryItems);
    }

    public PageResult<DictionaryItemVO> pageQuery(DictionaryPageQuery dictionaryPageQuery) {
        return null;
    }
}
