package com.ryqg.jiaofu.business.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FileInfo {

    @Schema(description = "文件名称")
    private String name;

    @Schema(description = "文件URL")
    private String url;

}
