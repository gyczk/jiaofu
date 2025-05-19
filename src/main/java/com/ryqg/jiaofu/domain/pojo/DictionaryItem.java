package com.ryqg.jiaofu.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ryqg.jiaofu.business.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dictionaries_items")
public class DictionaryItem extends BaseModel {
    // 关联字典编码，与sys_dict表中的dict_code对应
    private String dictCode;

    // 字典项值
    private String value;

    // 字典项标签
    private String label;

    // 标签类型，用于前端样式展示（如success、warning等）
    private String tagType;

    // 状态（1-正常，0-禁用）
    private Integer status;

    // 排序
    private Integer sort;

    // 备注
    private String remark;

    // 创建人ID
    private String createBy;

    // 修改人ID
    private String updateBy;
}
