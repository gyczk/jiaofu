package com.ryqg.jiaofu.common.converter;

import com.ryqg.jiaofu.domain.dto.PDFDropdownDTO;
import com.ryqg.jiaofu.domain.pojo.PDFDropdown;
import com.ryqg.jiaofu.domain.vo.PDFDropdownVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PDFDropdownConverter extends BaseConverter<PDFDropdown, PDFDropdownDTO, PDFDropdownVO>{


}
