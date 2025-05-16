package com.ryqg.jiaofu.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_users_roles")
@Data
public class UserRole {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色ID
     */
    private String roleId;
}
