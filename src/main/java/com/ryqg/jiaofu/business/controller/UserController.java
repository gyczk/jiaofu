package com.ryqg.jiaofu.business.controller;

import com.ryqg.jiaofu.business.common.BaseController;
import com.ryqg.jiaofu.business.service.UserService;
import com.ryqg.jiaofu.common.Result;
import com.ryqg.jiaofu.domain.dto.UserDTO;
import com.ryqg.jiaofu.domain.vo.CurrentUserVO;
import com.ryqg.jiaofu.domain.vo.UserVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseController<UserService, UserDTO, UserVO> {

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
}
