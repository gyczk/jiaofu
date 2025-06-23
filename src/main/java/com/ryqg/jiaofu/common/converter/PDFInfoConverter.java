package com.ryqg.jiaofu.common.converter;

import com.ryqg.jiaofu.domain.dto.PDFInfoDTO;
import com.ryqg.jiaofu.domain.pojo.PDFInfo;
import com.ryqg.jiaofu.domain.vo.PDFInfoVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PDFInfoConverter extends BaseConverter<PDFInfo, PDFInfoDTO, PDFInfoVO>{


}
