package com.ryqg.jiaofu.business.controller;

import com.ryqg.jiaofu.business.common.BaseController;
import com.ryqg.jiaofu.business.service.RoleService;
import com.ryqg.jiaofu.domain.dto.RoleDTO;
import com.ryqg.jiaofu.domain.vo.RoleVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController extends BaseController<RoleService, RoleDTO, RoleVO> {

}
