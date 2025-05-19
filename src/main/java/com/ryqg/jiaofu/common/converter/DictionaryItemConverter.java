package com.ryqg.jiaofu.common.converter;

import com.ryqg.jiaofu.domain.dto.DictionaryItemDTO;
import com.ryqg.jiaofu.domain.model.Option;
import com.ryqg.jiaofu.domain.pojo.DictionaryItem;
import com.ryqg.jiaofu.domain.vo.DictionaryItemVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DictionaryItemConverter extends BaseConverter<DictionaryItem, DictionaryItemDTO, DictionaryItemVO>{
    @Mappings({
            @Mapping(target = "value", source = "value"),
            @Mapping(target = "label", source = "label")
    })
    Option<String> toOption(DictionaryItem DictionaryItem);

    List<Option<String>> toOptions(List<DictionaryItem> DictionaryItems);
}
