package com.ryqg.jiaofu.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "键值对")
@Data
@NoArgsConstructor
public class KeyValue {

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Schema(description = "选项的值")
    private String key;

    @Schema(description = "选项的标签")
    private String value;

}
