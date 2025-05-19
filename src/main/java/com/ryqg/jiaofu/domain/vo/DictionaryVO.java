package com.ryqg.jiaofu.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DictionaryVO extends BaseVO {
    // 类型编码
    private String dictCode;

    // 类型名称
    private String name;

    // 状态(0:正常;1:禁用)
    private Integer status;

    // 备注
    private String remark;
}
