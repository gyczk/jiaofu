package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.db.sql.Direction;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.business.common.ServiceImpl;
import com.ryqg.jiaofu.business.mapper.MenuMapper;
import com.ryqg.jiaofu.business.service.MenuService;
import com.ryqg.jiaofu.common.converter.MenuConverter;
import com.ryqg.jiaofu.domain.dto.MenuDTO;
import com.ryqg.jiaofu.domain.pojo.Menu;
import com.ryqg.jiaofu.domain.vo.MenuVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuConverter, Menu, MenuDTO, MenuVO> implements MenuService {

    @Override
    public PageResult<MenuVO> pageQuery(cn.hutool.db.Page pageParam, MenuDTO dto) {
        Page<Menu> page = Page.of(pageParam.getPageNumber(), pageParam.getPageSize());
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (dto != null) {
            queryWrapper.lambda().like(StringUtils.isNotBlank(dto.getName()), Menu::getName, dto.getName());
        }
        Arrays.stream(pageParam.getOrders()).forEach(item -> {
            queryWrapper.orderBy(true, Direction.ASC.equals(item.getDirection()), item.getField());
        });
        page = baseMapper.selectPage(page, queryWrapper);
        return baseConverter.toPageResult(page);
    }

    @Override
    public List<MenuVO> listMenus(MenuDTO menuDTO) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(menuDTO.getName()), Menu::getName, menuDTO.getName())
                .orderByAsc(Menu::getSort);
        List<Menu> menus = baseMapper.selectList(queryWrapper);

        // 获取所有菜单ID
        Set<String> menuIds = menus.stream()
                .map(Menu::getId)
                .collect(Collectors.toSet());

        // 获取所有父级ID
        Set<String> parentIds = menus.stream()
                .map(Menu::getParentId)
                .collect(Collectors.toSet());

        // 获取根节点ID（递归的起点），即父节点ID中不包含在部门ID中的节点，注意这里不能拿顶级菜单 O 作为根节点，因为菜单筛选的时候 O 会被过滤掉
        List<String> rootIds = parentIds.stream()
                .filter(id -> !menuIds.contains(id))
                .toList();

        // 使用递归函数来构建菜单树
        return rootIds.stream()
                .flatMap(rootId -> buildMenuTree(rootId, menus).stream())
                .collect(Collectors.toList());
    }

    private List<MenuVO> buildMenuTree(String parentId, List<Menu> menuList) {
        return CollectionUtil.emptyIfNull(menuList)
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(entity -> {
                    MenuVO menuVO = baseConverter.toVO(entity);
                    List<MenuVO> children = buildMenuTree(entity.getId(), menuList);
                    menuVO.setChildren(children);
                    return menuVO;
                }).toList();
    }
}
