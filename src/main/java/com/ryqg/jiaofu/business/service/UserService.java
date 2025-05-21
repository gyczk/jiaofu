package com.ryqg.jiaofu.business.service;

import com.ryqg.jiaofu.business.common.IBaseService;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.domain.PageQuery.UserPageQuery;
import com.ryqg.jiaofu.domain.dto.UserDTO;
import com.ryqg.jiaofu.domain.vo.CurrentUserVO;
import com.ryqg.jiaofu.domain.vo.UserVO;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends IBaseService<UserDTO, UserVO> {

    CurrentUserVO getMe();

    UserVO getUserForm(String userId);

    PageResult<UserVO> pageQuery(UserPageQuery userPageQuery);
}
