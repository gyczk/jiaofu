package com.ryqg.jiaofu.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_roles_menus")
@Data
public class RoleMenu {
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;
}
