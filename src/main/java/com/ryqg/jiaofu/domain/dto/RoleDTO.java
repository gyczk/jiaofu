package com.ryqg.jiaofu.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleDTO extends BaseDTO {
    private String id;
    // 角色名称
    private String name;

    // 角色编码
    private String code;

    // 显示顺序
    private String sort;

    // 角色状态(1-正常 0-停用)
    private String status;

    // 数据权限(1-所有数据 2-部门及子部门数据 3-本部门数据 4-本人数据)
    private String dataScope;
}
