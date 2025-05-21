package com.ryqg.jiaofu.business.service;

import com.ryqg.jiaofu.business.common.IBaseService;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.domain.PageQuery.MenuPageQuery;
import com.ryqg.jiaofu.domain.dto.MenuDTO;
import com.ryqg.jiaofu.domain.model.Option;
import com.ryqg.jiaofu.domain.vo.MenuVO;
import com.ryqg.jiaofu.domain.vo.RouteVO;

import java.util.List;

public interface MenuService extends IBaseService<MenuDTO, MenuVO> {
    List<MenuVO> listMenus(MenuPageQuery menuPageQuery);

    List<RouteVO> getCurrentUserRoutes();

    default PageResult<MenuVO> pageQuery(MenuPageQuery menuPageQuery){
        throw new UnsupportedOperationException("菜单接口无需分页查询");
    }
    List<Option<String>> listMenuOptions(boolean onlyParent);

    MenuVO getMenuForm(String id);
}
