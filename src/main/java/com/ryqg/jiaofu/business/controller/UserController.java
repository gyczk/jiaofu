package com.ryqg.jiaofu.business.controller;

import com.ryqg.jiaofu.common.Result;
import com.ryqg.jiaofu.common.ResultCode;
import com.ryqg.jiaofu.pojo.User;
import com.ryqg.jiaofu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public User getUserInfo(@PathVariable String userId) {
        int i = 1/0;
        return userService.getUserInfo(userId);
    }

    @PostMapping
    public Result<?> register(@RequestBody User user) {
        int flag = userService.save(user);
        if (flag != 1) {
            return Result.failed(ResultCode.USER_REGISTRATION_ERROR);
        }
        return Result.success();
    }
}
