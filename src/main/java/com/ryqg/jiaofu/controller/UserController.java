package com.ryqg.jiaofu.controller;

import com.ryqg.jiaofu.pojo.User;
import com.ryqg.jiaofu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public User getUserInfo(@PathVariable String userId) {
        return userService.getUserInfo(userId);
    }

    @PostMapping("/save")
    public String register(@RequestBody User user) {
        userService.save(user);
        return "success";
    }
}
