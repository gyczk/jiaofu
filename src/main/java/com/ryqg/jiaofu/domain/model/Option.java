package com.ryqg.jiaofu.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 下拉选项对象
 */
@Data
@NoArgsConstructor
public class Option<T> implements Serializable {

    public Option(T value, String label) {
        this.value = value;
        this.label = label;
    }

    public Option(T value, String label, List<Option<T>> children) {
        this.value = value;
        this.label = label;
        this.children= children;
    }

    public Option(T value, String label, String tagType) {
        this.value = value;
        this.label = label;
        this.tagType= tagType;
    }


    @Schema(description="选项的值")
    private T value;

    @Schema(description="选项的标签")
    private String label;

    @Schema(description = "标签类型")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String tagType;

    @Schema(description="子选项列表")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private List<Option<T>> children;

}