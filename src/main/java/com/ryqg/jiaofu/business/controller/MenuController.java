package com.ryqg.jiaofu.business.controller;

import com.ryqg.jiaofu.business.common.BaseController;
import com.ryqg.jiaofu.business.service.MenuService;
import com.ryqg.jiaofu.common.Result;
import com.ryqg.jiaofu.domain.dto.MenuDTO;
import com.ryqg.jiaofu.domain.vo.MenuVO;
import com.ryqg.jiaofu.domain.vo.RouteVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController extends BaseController<MenuService, MenuDTO, MenuVO> {
    private final MenuService menuService;

    @GetMapping
    public Result<List<MenuVO>> listMenus(MenuDTO menuDTO) {
        List<MenuVO> menuList = menuService.listMenus(menuDTO);
        return Result.success(menuList);
    }

    @GetMapping("/routes")
    public Result<List<RouteVO>> getCurrentUserRoutes() {
        List<RouteVO> routeList = menuService.getCurrentUserRoutes();
        return Result.success(routeList);
    }
}
