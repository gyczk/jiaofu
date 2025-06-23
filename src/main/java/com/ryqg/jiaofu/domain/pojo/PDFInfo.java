package com.ryqg.jiaofu.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ryqg.jiaofu.business.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_pdf_infos")
public class PDFInfo extends BaseModel {
    private String name;
    private String author;
    private double coin;
    private double size;
    private int totalPage;
    private int type;
    private int format;
    private String classify;
    private int views;
    private int downloads;
    private String doUrl;
    private String coverUrl;
}
