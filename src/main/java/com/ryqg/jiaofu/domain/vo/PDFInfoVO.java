package com.ryqg.jiaofu.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_pdf_infos")
public class PDFInfoVO extends BaseVO {
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
    private String coverUrl;
    private LocalDateTime createTime;
}
