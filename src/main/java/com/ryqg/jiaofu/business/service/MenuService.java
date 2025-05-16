package com.ryqg.jiaofu.business.service;

import com.ryqg.jiaofu.business.common.IBaseService;
import com.ryqg.jiaofu.domain.dto.MenuDTO;
import com.ryqg.jiaofu.domain.vo.MenuVO;

import java.util.List;

public interface MenuService extends IBaseService<MenuDTO, MenuVO> {
    List<MenuVO> listMenus(MenuDTO menuDTO);
}
