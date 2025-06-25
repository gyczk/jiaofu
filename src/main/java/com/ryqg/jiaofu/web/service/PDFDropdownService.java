package com.ryqg.jiaofu.web.service;

import com.ryqg.jiaofu.domain.dto.PDFDropdownDTO;
import com.ryqg.jiaofu.domain.vo.PDFDropdownVO;

import java.util.List;

public interface PDFDropdownService {
    List<PDFDropdownVO> getDropdown();

    void addDropdown(PDFDropdownDTO pdfDropdownDTO);

    PDFDropdownVO getFormData(String id);

    void updateDropdown(PDFDropdownDTO pdfDropdownDTO);

    void deleteDropdown(String id);
}
