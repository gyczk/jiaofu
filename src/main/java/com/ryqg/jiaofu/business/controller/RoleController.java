package com.ryqg.jiaofu.business.controller;

import com.ryqg.jiaofu.business.common.BaseController;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.business.service.RoleService;
import com.ryqg.jiaofu.common.Result;
import com.ryqg.jiaofu.domain.PageQuery.RolePageQuery;
import com.ryqg.jiaofu.domain.dto.RoleDTO;
import com.ryqg.jiaofu.domain.model.Option;
import com.ryqg.jiaofu.domain.vo.RoleVO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController extends BaseController<RoleService, RoleDTO, RoleVO> {

    @GetMapping("/options")
    public Result<List<Option<String>>> RoleOptions(){
        List<Option<String>> roleOptions = baseService.getRoleOptions();
        return Result.success(roleOptions);
    }

    @GetMapping("/page")
    public Result<PageResult<RoleVO>> page(RolePageQuery rolePageQuery){
        PageResult<RoleVO> pageResult = baseService.pageQuery(rolePageQuery);
        return Result.success(pageResult);
    }

    @RequestMapping("{roleId}/form")
    public Result<RoleVO> getUserForm(@PathVariable String roleId){
        RoleVO roleVO = baseService.getRoleForm(roleId);
        return Result.success(roleVO);
    }

    @GetMapping("/{roleId}/menuIds")
    public Result<List<String>> getRoleMenuIds(
            @Parameter(description = "角色ID") @PathVariable String roleId) {
        List<String> menuIds = baseService.getRoleMenuIds(roleId);
        return Result.success(menuIds);
    }

    @PutMapping("/{roleId}/menus")
    public Result<Void> assignMenusToRole(
            @PathVariable String roleId,
            @RequestBody List<String> menuIds) {
        baseService.assignMenusToRole(roleId, menuIds);
        return Result.success();
    }
}
