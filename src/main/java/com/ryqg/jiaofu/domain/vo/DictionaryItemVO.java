package com.ryqg.jiaofu.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DictionaryItemVO extends BaseVO {

    // 字典项值
    private String value;

    // 字典项标签
    private String label;

    // 状态（1-正常，0-禁用）
    private Integer status;

    // 排序
    private Integer sort;
}
