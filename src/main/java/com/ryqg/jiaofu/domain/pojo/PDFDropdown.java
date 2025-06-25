package com.ryqg.jiaofu.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ryqg.jiaofu.business.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_pdf_dropdowns")
public class PDFDropdown extends BaseModel {
    private String parentId;
    private String treePath;
    private String name;
    private int parent;
    private int sort;
}
