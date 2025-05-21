package com.ryqg.jiaofu.business.service;

import com.ryqg.jiaofu.business.common.IBaseService;
import com.ryqg.jiaofu.business.common.PageResult;
import com.ryqg.jiaofu.domain.PageQuery.RolePageQuery;
import com.ryqg.jiaofu.domain.dto.RoleDTO;
import com.ryqg.jiaofu.domain.model.Option;
import com.ryqg.jiaofu.domain.vo.RoleVO;

import java.util.List;

public interface RoleService extends IBaseService<RoleDTO, RoleVO> {
    List<Option<String>> getRoleOptions();

    PageResult<RoleVO> pageQuery(RolePageQuery rolePageQuery);
}
