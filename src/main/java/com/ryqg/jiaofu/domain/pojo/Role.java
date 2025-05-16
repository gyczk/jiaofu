package com.ryqg.jiaofu.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ryqg.jiaofu.business.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_roles")
public class Role extends BaseModel {
    // 角色名称
    private String name;

    // 角色编码
    private String code;

    // 显示顺序
    private Integer sort;

    // 角色状态(1-正常 0-停用)
    private Integer status;

    // 数据权限(1-所有数据 2-部门及子部门数据 3-本部门数据 4-本人数据)
    private Integer dataScope;

    // 创建人 ID
    private String createBy;

    // 更新人ID
    private String updateBy;

    // 逻辑删除标识(0-未删除 1-已删除)
    private Integer isDeleted;
}
