package com.ryqg.jiaofu.common.constants;

import lombok.Getter;

@Getter
public enum StatusEnum {
    ENABLE(1, "启用"),
    DISABLE (0, "禁用");

    private final Integer value;


    private final String label;

    StatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
