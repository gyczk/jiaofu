package com.ryqg.jiaofu.business.controller;

import com.ryqg.jiaofu.business.common.BaseController;
import com.ryqg.jiaofu.business.service.DictionaryItemService;
import com.ryqg.jiaofu.domain.dto.DictionaryItemDTO;
import com.ryqg.jiaofu.domain.vo.DictionaryItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dictItems")
@RequiredArgsConstructor
public class DictionaryItemController extends BaseController<DictionaryItemService, DictionaryItemDTO, DictionaryItemVO> {

}
