package com.ryqg.jiaofu.common.converter;

import com.ryqg.jiaofu.domain.dto.DictionaryDTO;
import com.ryqg.jiaofu.domain.model.Option;
import com.ryqg.jiaofu.domain.pojo.Dictionary;
import com.ryqg.jiaofu.domain.pojo.Role;
import com.ryqg.jiaofu.domain.vo.DictionaryVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DictionaryConverter extends BaseConverter<Dictionary, DictionaryDTO, DictionaryVO>{
}
