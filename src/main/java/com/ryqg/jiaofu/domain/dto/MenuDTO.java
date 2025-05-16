package com.ryqg.jiaofu.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MenuDTO extends BaseDTO {
    // 父菜单ID
    private String  parentId;

    // 父节点ID路径
    private String  treePath;

    // 菜单名称
    private String  name;

    // 菜单类型（1-菜单 2-目录 3-外链 4-按钮）
    private Integer  type;

    // 路由名称（Vue Router 中用于命名路由）
    private String  routeName;

    // 路由路径（Vue Router 中定义的 URL 路径）
    private String  routePath;

    // 组件路径（组件页面完整路径，相对于 src/views/，缺省后缀 .vue）
    private String  component;

    // 【按钮】权限标识
    private String  perm;

    // 【目录】只有一个子路由是否始终显示（1-是 0-否）
    private Integer  alwaysShow;

    // 【菜单】是否开启页面缓存（1-是 0-否）
    private Integer  keepAlive;

    // 显示状态（1-显示 0-隐藏）
    private Integer  visible;

    // 排序
    private Integer  sort;

    // 菜单图标
    private String  icon;

    // 跳转路径
    private String  redirect;

    // 路由参数
    private String  params;
}
