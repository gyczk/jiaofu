package com.ryqg.jiaofu.web.mapper;

import com.ryqg.jiaofu.domain.pojo.PDFInfo;
import com.ryqg.jiaofu.domain.pojo.PDFView;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PDFMapper {
    List<PDFInfo> selectPDFList();

    PDFView selectViewByDocId(String docId);
}
