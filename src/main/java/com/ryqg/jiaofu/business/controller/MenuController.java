package com.ryqg.jiaofu.business.controller;

import com.ryqg.jiaofu.business.common.BaseController;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.business.service.MenuService;
import com.ryqg.jiaofu.common.Result;
import com.ryqg.jiaofu.domain.PageQuery.MenuPageQuery;
import com.ryqg.jiaofu.domain.dto.MenuDTO;
import com.ryqg.jiaofu.domain.model.Option;
import com.ryqg.jiaofu.domain.vo.MenuVO;
import com.ryqg.jiaofu.domain.vo.RouteVO;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController extends BaseController<MenuService, MenuDTO, MenuVO> {
    @GetMapping
    public Result<List<MenuVO>> listMenus(MenuPageQuery menuPageQuery) {
        List<MenuVO> menuList = baseService.listMenus(menuPageQuery);
        return Result.success(menuList);
    }

    @GetMapping("/routes")
    public Result<List<RouteVO>> getCurrentUserRoutes() {
        List<RouteVO> routeList = baseService.getCurrentUserRoutes();
        return Result.success(routeList);
    }

    @GetMapping("/page")
    public Result<PageResult<MenuVO>> page(MenuPageQuery menuPageQuery) {
        PageResult<MenuVO> pageResult = baseService.pageQuery(menuPageQuery);
        return Result.success(pageResult);
    }

    // 创建
    @Override
    @PostMapping
    public Result<Void> create(@RequestBody MenuDTO dto) {
        baseService.update(dto);
        return Result.success();
    }

    @GetMapping("/options")
    public Result<List<Option<String>>> listMenuOptions(
            @Parameter(description = "是否只查询父级菜单")
            @RequestParam(required = false, defaultValue = "false") boolean onlyParent) {
        List<Option<String>> menus = baseService.listMenuOptions(onlyParent);
        return Result.success(menus);
    }

    @GetMapping("/{id}/form")
    public Result<MenuVO> getMenuForm(
            @Parameter(description = "菜单ID") @PathVariable String id) {
        MenuVO menu = baseService.getMenuForm(id);
        return Result.success(menu);
    }
}
