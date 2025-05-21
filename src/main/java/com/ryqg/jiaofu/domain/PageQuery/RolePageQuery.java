package com.ryqg.jiaofu.domain.PageQuery;

import com.ryqg.jiaofu.business.common.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RolePageQuery extends Page {
    private String name;
}
