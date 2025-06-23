package com.ryqg.jiaofu.web.controller;

import com.ryqg.jiaofu.common.Result;
import com.ryqg.jiaofu.domain.vo.PDFInfoVO;
import com.ryqg.jiaofu.web.service.PDFService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/v1/pdf")
@AllArgsConstructor
public class PDFController {

    private final PDFService pdfService;

    @GetMapping("/view/{docId}")
    @ResponseBody
    public Result<String> pdfContent(@PathVariable String docId) {
        String html = pdfService.getPDFView(docId);
        return Result.success(html);
    }

    @ResponseBody
    @GetMapping("/list")
    public Result<List<PDFInfoVO>> pdfList() {
        List<PDFInfoVO> pdfViewVOList = pdfService.getPDFList();
        return Result.success(pdfViewVOList);
    }

}
