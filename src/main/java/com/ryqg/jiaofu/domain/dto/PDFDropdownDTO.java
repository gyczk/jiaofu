package com.ryqg.jiaofu.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PDFDropdownDTO extends BaseDTO {
    private String parentId;
    private String name;
    private int sort;
    private int parent;
}
