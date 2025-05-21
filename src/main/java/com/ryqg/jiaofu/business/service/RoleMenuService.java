package com.ryqg.jiaofu.business.service;

import java.util.List;

public interface RoleMenuService {
    List<String> getMenuIds(String roleId);

    /**
     * 根据roleId删除
     *
     * @param roleIds 角色ID
     * @return
     */
    void deleteByRoleIds(String roleIds);

    void saveRoleMenus(String roleId, List<String> menuIds);
}
