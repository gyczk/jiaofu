package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryqg.jiaofu.business.common.ServiceImpl;
import com.ryqg.jiaofu.business.mapper.MenuMapper;
import com.ryqg.jiaofu.business.service.MenuService;
import com.ryqg.jiaofu.common.constants.MenuTypeEnum;
import com.ryqg.jiaofu.common.constants.SecurityConstants;
import com.ryqg.jiaofu.common.constants.StatusEnum;
import com.ryqg.jiaofu.common.converter.MenuConverter;
import com.ryqg.jiaofu.domain.PageQuery.MenuPageQuery;
import com.ryqg.jiaofu.domain.dto.MenuDTO;
import com.ryqg.jiaofu.domain.model.KeyValue;
import com.ryqg.jiaofu.domain.model.Option;
import com.ryqg.jiaofu.domain.pojo.Menu;
import com.ryqg.jiaofu.domain.vo.MenuVO;
import com.ryqg.jiaofu.domain.vo.RouteVO;
import com.ryqg.jiaofu.utils.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuConverter, Menu, MenuDTO, MenuVO> implements MenuService {
    private final PermissionCacheService permissionCacheService;
    @Override
    public List<Option<String>> listMenuOptions(boolean onlyParent) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.in(onlyParent, Menu::getType, MenuTypeEnum.MENU.getValue(), MenuTypeEnum.CATALOG.getValue())
                .orderByAsc(Menu::getSort);
        List<Menu> menuList = baseMapper.selectList(menuLambdaQueryWrapper);
        return buildMenuOptions(SecurityConstants.ROOT_NODE_ID, menuList);
    }

    @Override
    public MenuVO getMenuForm(String id) {
        Menu entity = baseMapper.selectById(id);
        MenuVO menuVO = baseConverter.toVO(entity);
        if (ObjectUtil.isNotNull(entity)) {
            // 路由参数字符串 {"id":"1","name":"张三"} 转换为 [{key:"id", value:"1"}, {key:"name", value:"张三"}]
            String params = entity.getParams();
            if (StrUtil.isNotBlank(params)) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    // 解析 JSON 字符串为 Map<String, String>
                    Map<String, String> paramMap = objectMapper.readValue(params, new TypeReference<>() {
                    });

                    // 转换为 List<KeyValue> 格式 [{key:"id", value:"1"}, {key:"name", value:"张三"}]
                    List<KeyValue> transformedList = paramMap.entrySet().stream()
                            .map(entry -> new KeyValue(entry.getKey(), entry.getValue()))
                            .toList();

                    // 将转换后的列表存入 MenuForm
                    menuVO.setParams(transformedList);
                } catch (Exception e) {
                    throw new RuntimeException("解析参数失败", e);
                }
            }
        }
        return menuVO;
    }

    /**
     * 递归生成菜单下拉层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return 菜单下拉列表
     */
    private List<Option<String>> buildMenuOptions(String parentId, List<Menu> menuList) {
        List<Option<String>> menuOptions = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menu.getParentId().equals(parentId)) {
                Option<String> option = new Option<>(menu.getId(), menu.getName());
                List<Option<String>> subMenuOptions = buildMenuOptions(menu.getId(), menuList);
                if (!subMenuOptions.isEmpty()) {
                    option.setChildren(subMenuOptions);
                }
                menuOptions.add(option);
            }
        }

        return menuOptions;
    }

    @Override
    public void delete(String id) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new LambdaQueryWrapper<Menu>().eq(Menu::getId, id).or()
                .apply("CONCAT (',',tree_path,',') LIKE CONCAT('%,',{0},',%')", id);
        baseMapper.delete(lambdaQueryWrapper);
        permissionCacheService.refreshPermissions();
    }

    @Override
    public List<MenuVO> listMenus(MenuPageQuery menuPageQuery) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(StringUtils.isNotBlank(menuPageQuery.getName()), Menu::getName, menuPageQuery.getName())
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
    public void update(MenuDTO menuDTO) {
        Integer menuType = menuDTO.getType();

        if (MenuTypeEnum.CATALOG.getValue().equals(menuType)) {  // 如果是目录
            String path = menuDTO.getRoutePath();
            if ("0".equals(menuDTO.getParentId()) && !path.startsWith("/")) {
                menuDTO.setRoutePath("/" + path); // 一级目录需以 / 开头
            }
            menuDTO.setComponent("Layout");
        } else if (MenuTypeEnum.EXTERNAL_LINK.getValue().equals(menuType)) {
            // 外链菜单组件设置为 null
            menuDTO.setComponent(null);
        }
        if (Objects.equals(menuDTO.getParentId(), menuDTO.getId())) {
            throw new RuntimeException("父级菜单不能为当前菜单");
        }
        Menu entity = baseConverter.toEntity(menuDTO);
        String treePath = generateMenuTreePath(menuDTO.getParentId());
        entity.setTreePath(treePath);

        List<KeyValue> params = menuDTO.getParams();
        // 路由参数 [{key:"id",value:"1"}，{key:"name",value:"张三"}] 转换为 [{"id":"1"},{"name":"张三"}]
        if (CollectionUtil.isNotEmpty(params)) {
            entity.setParams(JSONUtil.toJsonStr(params.stream()
                    .collect(Collectors.toMap(KeyValue::getKey, KeyValue::getValue))));
        } else {
            entity.setParams(null);
        }
        // 新增类型为菜单时候 路由名称唯一
        if (MenuTypeEnum.MENU.getValue().equals(menuType)) {
            Assert.isFalse(baseMapper.exists(new LambdaQueryWrapper<Menu>()
                    .eq(Menu::getRouteName, entity.getRouteName())
                    .ne(menuDTO.getId() != null, Menu::getId, menuDTO.getId())
            ), "路由名称已存在");
        } else {
            // 其他类型时 给路由名称赋值为空
            entity.setRouteName(null);
        }

        if (ObjectUtil.isNull(entity.getId())) {
            baseMapper.insert(entity);
        } else {
            baseMapper.updateById(entity);
            // 编辑刷新角色权限缓存
            permissionCacheService.refreshPermissions();
        }

        // 修改菜单如果有子菜单(菜单移动)，则更新子菜单的树路径
        updateChildrenTreePath(entity.getId(), treePath);
    }

    private String generateMenuTreePath(String parentId) {
        if (SecurityConstants.ROOT_NODE_ID.equals(parentId)) {
            return parentId;
        } else {
            Menu parent = baseMapper.selectById(parentId);
            return parent != null ? parent.getTreePath() + "," + parent.getId() : null;
        }
    }

    private void updateChildrenTreePath(String id, String treePath) {
        List<Menu> children = baseMapper.selectList(new LambdaQueryWrapper<Menu>().eq(Menu::getParentId, id));
        if (CollectionUtil.isNotEmpty(children)) {
            // 子菜单的树路径等于父菜单的树路径加上父菜单ID
            String childTreePath = treePath + "," + id;
            baseMapper.update(new LambdaUpdateWrapper<Menu>()
                    .eq(Menu::getParentId, id)
                    .set(Menu::getTreePath, childTreePath)
            );
            for (Menu child : children) {
                // 递归更新子菜单
                updateChildrenTreePath(child.getId(), childTreePath);
            }
        }
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

   /* private List<MenuVO> buildMenuTree(String parentId, List<Menu> menuList) {
        return CollectionUtil.emptyIfNull(menuList)
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(entity -> {
                    MenuVO menuVO = baseConverter.toVO(entity);
                    List<MenuVO> children = buildMenuTree(entity.getId(), menuList);
                    menuVO.setChildren(children);
                    return menuVO;
                }).toList();
    }*/

    public List<MenuVO> buildMenuTree(String parentId, List<Menu> menuList) {
        // 1. 将 Menu 转换为 MenuVO，并建立 ID 到 MenuVO 的映射
        Map<String, MenuVO> menuMap = menuList.stream()
                .map(baseConverter::toVO)
                .peek(menuVO -> menuVO.setChildren(new ArrayList<>())) // 预先初始化 children 列表
                .collect(Collectors.toMap(MenuVO::getId, v -> v,(existing, replacement) -> existing, // 处理键冲突的合并函数
                LinkedHashMap::new ));// 指定有序 Map 实现

        // 2. 构建菜单树结构
        List<MenuVO> rootMenus = new ArrayList<>();
        for (MenuVO menu : menuMap.values()) {
            if (menu.getParentId().equals(parentId)) {
                rootMenus.add(menu);
            } else {
                MenuVO parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    parent.getChildren().add(menu);
                }
            }
        }

        return rootMenus;
    }
}
