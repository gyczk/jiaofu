package com.ryqg.jiaofu.business.service;

import java.util.List;

public interface UserRoleService {
    /**
     * 保存用户角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    void saveUserRoles(String userId, List<String> roleIds);

    /**
     * 判断角色是否存在绑定用户
     *
     * @param roleId 角色ID
     * @return true：已分配 false：未分配
     */
    boolean hasAssignedUsers(String roleId);
}
