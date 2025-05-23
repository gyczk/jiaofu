package com.ryqg.jiaofu.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class RolePermsVO implements Serializable {
    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 权限标识集合
     */
    private Set<String> perms;
}
