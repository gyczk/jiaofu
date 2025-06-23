package com.ryqg.jiaofu.web.service;


import com.ryqg.jiaofu.domain.vo.PDFInfoVO;

import java.util.List;

public interface PDFService {
    String getPDFView(String docId);

    List<PDFInfoVO> getPDFList();
}
