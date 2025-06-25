package com.ryqg.jiaofu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryqg.jiaofu.domain.pojo.PDFDropdown;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PDFDropdownMapper extends BaseMapper<PDFDropdown> {
    List<PDFDropdown> getDropdown();
}
