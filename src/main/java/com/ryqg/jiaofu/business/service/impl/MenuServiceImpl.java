package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.sql.Direction;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.business.common.ServiceImpl;
import com.ryqg.jiaofu.business.mapper.MenuMapper;
import com.ryqg.jiaofu.business.service.MenuService;
import com.ryqg.jiaofu.common.constants.MenuTypeEnum;
import com.ryqg.jiaofu.common.constants.SecurityConstants;
import com.ryqg.jiaofu.common.constants.StatusEnum;
import com.ryqg.jiaofu.common.converter.MenuConverter;
import com.ryqg.jiaofu.domain.PageQuery.MenuPageQuery;
import com.ryqg.jiaofu.domain.dto.MenuDTO;
import com.ryqg.jiaofu.domain.pojo.Menu;
import com.ryqg.jiaofu.domain.vo.MenuVO;
import com.ryqg.jiaofu.domain.vo.RouteVO;
import com.ryqg.jiaofu.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuConverter, Menu, MenuDTO, MenuVO> implements MenuService {
    public PageResult<MenuVO> pageQuery(MenuPageQuery menuPageQuery) {
        Page<Menu> page = Page.of(menuPageQuery.getPageNumber(), menuPageQuery.getPageSize());
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isBlankIfStr(menuPageQuery.getName())) {
            queryWrapper.lambda().like(StringUtils.isNotBlank(menuPageQuery.getName()), Menu::getName, menuPageQuery.getName());
        }
        if (ArrayUtil.isNotEmpty(menuPageQuery.getOrders())){
            Arrays.stream(menuPageQuery.getOrders()).forEach(item -> {
                queryWrapper.orderBy(true, Direction.ASC.equals(item.getDirection()), item.getField());
            });
        }
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

    @Override
    public List<RouteVO> getCurrentUserRoutes() {
        Set<String> roleCodes = SecurityUtils.getRoles();

        if (CollectionUtil.isEmpty(roleCodes)) {
            return Collections.emptyList();
        }
        List<Menu> menuList;
        if (SecurityUtils.isRoot()) {
            // 超级管理员获取所有菜单
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().ne(Menu::getType, MenuTypeEnum.BUTTON.getValue()).orderByAsc(Menu::getSort);
            menuList = baseMapper.selectList(queryWrapper);

        } else {
            menuList = this.baseMapper.getMenusByRoleCodes(roleCodes);
        }
        return buildRoutes(SecurityConstants.ROOT_NODE_ID, menuList);
    }

    private List<RouteVO> buildRoutes(String parentId, List<Menu> menuList) {
        List<RouteVO> routeList = new ArrayList<>();

        for (Menu menu : menuList) {
            if (menu.getParentId().equals(parentId)) {
                RouteVO routeVO = toRouteVo(menu);
                List<RouteVO> children = buildRoutes(menu.getId(), menuList);
                if (!children.isEmpty()) {
                    routeVO.setChildren(children);
                }
                routeList.add(routeVO);
            }
        }

        return routeList;
    }

    private RouteVO toRouteVo(Menu menu) {
        RouteVO routeVO = new RouteVO();
        // 获取路由名称
        String routeName = menu.getRouteName();
        if (StrUtil.isBlank(routeName)) {
            // 路由 name 需要驼峰，首字母大写
            routeName = StringUtils.capitalize(StrUtil.toCamelCase(menu.getRoutePath(), '-'));
        }
        // 根据name路由跳转 this.$router.push({name:xxx})
        routeVO.setName(routeName);

        // 根据path路由跳转 this.$router.push({path:xxx})
        routeVO.setPath(menu.getRoutePath());
        routeVO.setRedirect(menu.getRedirect());
        routeVO.setComponent(menu.getComponent());

        RouteVO.Meta meta = new RouteVO.Meta();
        meta.setTitle(menu.getName());
        meta.setIcon(menu.getIcon());
        meta.setHidden(StatusEnum.DISABLE.getValue().equals(menu.getVisible()));
        // 【菜单】是否开启页面缓存
        if (MenuTypeEnum.MENU.getValue().equals(menu.getType())
                && ObjectUtil.equals(menu.getKeepAlive(), 1)) {
            meta.setKeepAlive(true);
        }
        meta.setAlwaysShow(ObjectUtil.equals(menu.getAlwaysShow(), 1));

        String paramsJson = menu.getParams();
        // 将 JSON 字符串转换为 Map<String, String>
        if (StrUtil.isNotBlank(paramsJson)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, String> paramMap = objectMapper.readValue(paramsJson, new TypeReference<>() {
                });
                meta.setParams(paramMap);
            } catch (Exception e) {
                throw new RuntimeException("解析参数失败", e);
            }
        }
        routeVO.setMeta(meta);
        return routeVO;
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
