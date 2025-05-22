package com.ryqg.jiaofu.business.controller;

import com.ryqg.jiaofu.business.common.BaseController;
import com.ryqg.jiaofu.business.service.DictionaryItemService;
import com.ryqg.jiaofu.business.service.DictionaryService;
import com.ryqg.jiaofu.common.Result;
import com.ryqg.jiaofu.domain.dto.DictionaryDTO;
import com.ryqg.jiaofu.domain.model.Option;
import com.ryqg.jiaofu.domain.vo.DictionaryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SuppressWarnings("SpellCheckingInspection")
@RequestMapping("/api/v1/dicts")
@RequiredArgsConstructor
public class DictionaryController extends BaseController<DictionaryService, DictionaryDTO, DictionaryVO> {
    private final DictionaryItemService dictionaryItemService;

    @GetMapping("/{id}/form")
    public Result<DictionaryVO> getDictForm(@Parameter(description = "字典ID") @PathVariable String id) {
        DictionaryVO dictionaryVO = baseService.getDictForm(id);
        return Result.success(dictionaryVO);
    }

    @Operation(summary = "字典项列表")
    @GetMapping("/{dictCode}/items")
    public Result<List<Option<String>>> getDictItems(
            @Parameter(description = "字典编码") @PathVariable String dictCode) {
        List<Option<String>> list = dictionaryItemService.getDictItems(dictCode);
        return Result.success(list);
    }
}
