package com.ryqg.jiaofu.business.controller;

import com.ryqg.jiaofu.business.common.BaseController;
import com.ryqg.jiaofu.business.service.UserService;
import com.ryqg.jiaofu.common.Result;
import com.ryqg.jiaofu.domain.dto.UserDTO;
import com.ryqg.jiaofu.domain.vo.UserVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseController<UserService, UserDTO, UserVO> {

    @RequestMapping("/me")
    public Result<UserVO> getMe(){
        UserVO userVo = baseService.getMe();
        return Result.success(userVo);
    }
}
