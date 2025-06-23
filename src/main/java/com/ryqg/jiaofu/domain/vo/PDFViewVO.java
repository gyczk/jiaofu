package com.ryqg.jiaofu.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PDFViewVO extends BaseVO{
    private String pdfId;

    private String htmlContent;
}
