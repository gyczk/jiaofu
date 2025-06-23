package com.ryqg.jiaofu.domain.pojo;

import com.ryqg.jiaofu.business.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PDFView extends BaseModel {
    private String pdfId;

    private String htmlContent;
}
