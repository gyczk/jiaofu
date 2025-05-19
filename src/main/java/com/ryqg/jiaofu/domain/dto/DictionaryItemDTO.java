package com.ryqg.jiaofu.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DictionaryItemDTO extends BaseDTO {
    // 类型名称
    private String name;
}
