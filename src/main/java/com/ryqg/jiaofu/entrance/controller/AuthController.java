package com.ryqg.jiaofu.entrance.controller;

import com.ryqg.jiaofu.common.Result;
import com.ryqg.jiaofu.config.security.token.AuthenticationToken;
import com.ryqg.jiaofu.entrance.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;


    @Operation(summary = "账号密码登录")
    @PostMapping("/login")
//    @Log(value = "登录", module = LogModuleEnum.LOGIN)
    public Result<AuthenticationToken> login(
            @Parameter(description = "手机号", example = "133xxxxxxx") @RequestParam String phone,
            @Parameter(description = "密码", example = "123456") @RequestParam String password
    ) {
        AuthenticationToken authenticationToken = authService.login(phone, password);
        return Result.success(authenticationToken);
    }

    @Operation(summary = "刷新访问令牌")
    @PostMapping("/refresh-token")
    public Result<?> refreshToken(
            @Parameter(description = "刷新令牌", example = "xxx.xxx.xxx") @RequestParam String refreshToken
    ) {
        AuthenticationToken authenticationToken = authService.refreshToken(refreshToken);
        return Result.success(authenticationToken);
    }
}
