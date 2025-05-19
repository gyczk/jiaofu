package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.db.Page;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.business.common.ServiceImpl;
import com.ryqg.jiaofu.business.mapper.DictionaryMapper;
import com.ryqg.jiaofu.business.service.DictionaryService;
import com.ryqg.jiaofu.common.converter.DictionaryConverter;
import com.ryqg.jiaofu.domain.dto.DictionaryDTO;
import com.ryqg.jiaofu.domain.pojo.Dictionary;
import com.ryqg.jiaofu.domain.vo.DictionaryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, DictionaryConverter, Dictionary, DictionaryDTO, DictionaryVO> implements DictionaryService {


    @Override
    public DictionaryVO getDictForm(String id) {
        Dictionary dictionary = baseMapper.selectById(id);
        return baseConverter.toVO(dictionary);
    }

    @Override
    public PageResult<DictionaryVO> pageQuery(Page pageParam, DictionaryDTO dto) {
        return null;
    }
}
