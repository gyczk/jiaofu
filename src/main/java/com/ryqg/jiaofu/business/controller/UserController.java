package com.ryqg.jiaofu.business.controller;

import com.ryqg.jiaofu.business.common.BaseController;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.business.service.UserService;
import com.ryqg.jiaofu.business.service.impl.LocalFileService;
import com.ryqg.jiaofu.common.Result;
import com.ryqg.jiaofu.domain.PageQuery.UserPageQuery;
import com.ryqg.jiaofu.domain.dto.UserDTO;
import com.ryqg.jiaofu.domain.vo.CurrentUserVO;
import com.ryqg.jiaofu.domain.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseController<UserService, UserDTO, UserVO> {

    private final LocalFileService localFileService;
    @RequestMapping("/me")
    public Result<CurrentUserVO> getMe(){
        CurrentUserVO userVo = baseService.getMe();
        return Result.success(userVo);
    }

    @RequestMapping("{userId}/form")
    public Result<UserVO> getUserForm(@PathVariable String userId){
        UserVO userVo = baseService.getUserForm(userId);
        return Result.success(userVo);
    }

    @GetMapping("/page")
    public Result<PageResult<UserVO>> page(UserPageQuery userPageQuery){
        PageResult<UserVO> pageResult = baseService.pageQuery(userPageQuery);
        return Result.success(pageResult);
    }

    @GetMapping("/profile")
    public Result<UserVO> getUserProfile() {
        UserVO userProfile = baseService.getUserProfile();
        return Result.success(userProfile);
    }

    @PutMapping("/profile")
    public Result<Void> updateUserProfile(@RequestBody UserDTO userDTO) {
        baseService.updateProfile(userDTO);
        return Result.success();
    }

}
