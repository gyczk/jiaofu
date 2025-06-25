package com.ryqg.jiaofu.web.controller;

import com.ryqg.jiaofu.common.Result;
import com.ryqg.jiaofu.domain.dto.PDFDropdownDTO;
import com.ryqg.jiaofu.domain.vo.PDFDropdownVO;
import com.ryqg.jiaofu.web.service.PDFDropdownService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/pdf-dropdown")
@AllArgsConstructor
public class PDFDropdownController {

    private final PDFDropdownService pdfDropdownService;

    @GetMapping
    @ResponseBody
    public Result<List<PDFDropdownVO>> getDropdown() {
        List<PDFDropdownVO> list = pdfDropdownService.getDropdown();
        return Result.success(list);
    }

    @PostMapping
    @ResponseBody
    public Result<List<PDFDropdownVO>> saveDropdown(@RequestBody PDFDropdownDTO pdfDropdownDTO) {
        pdfDropdownService.addDropdown(pdfDropdownDTO);
        return Result.success(List.of());
    }

    @ResponseBody
    @RequestMapping("/{id}/form")
    public Result<PDFDropdownVO> getFormData(@PathVariable String id) {
        PDFDropdownVO pdfDropdownVO = pdfDropdownService.getFormData(id);
        return Result.success(pdfDropdownVO);
    }

    @PutMapping
    @ResponseBody
    public Result<Void> updateDropdown(@RequestBody PDFDropdownDTO pdfDropdownDTO) {
        pdfDropdownService.updateDropdown(pdfDropdownDTO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Result<Void> deleteDropdown(@PathVariable String id) {
        pdfDropdownService.deleteDropdown(id);
        return Result.success();
    }

}
