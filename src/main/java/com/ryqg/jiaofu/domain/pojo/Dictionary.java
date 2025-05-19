package com.ryqg.jiaofu.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ryqg.jiaofu.business.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dictionaries")
public class Dictionary extends BaseModel {
    // 类型编码
    private String dictCode;

    // 类型名称
    private String name;

    // 状态(0:正常;1:禁用)
    private Integer status;

    // 备注
    private String remark;

    // 创建人ID
    private String createBy;

    // 修改人ID
    private String updateBy;

    // 是否删除(1-删除，0-未删除)
    private Integer isDeleted;
}
