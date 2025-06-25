package com.ryqg.jiaofu.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PDFDropdownVO extends BaseVO {
    private String parentId;
    private String treePath;
    private String name;
    private int parent;
    private int sort;
    private List<PDFDropdownVO> children;
}
