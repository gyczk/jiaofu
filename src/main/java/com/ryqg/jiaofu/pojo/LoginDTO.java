package com.ryqg.jiaofu.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class LoginDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4393557997355879737L;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "令牌")
    private String token;
}
