package com.ryqg.jiaofu.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleVO extends BaseVO {
    // 角色名称
    private String name;

    // 角色编码
    private String code;

    // 显示顺序
    private Integer sort;

    // 角色状态(1-正常 0-停用)
    private Integer status;
}
