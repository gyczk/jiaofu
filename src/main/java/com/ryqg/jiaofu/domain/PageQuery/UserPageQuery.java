package com.ryqg.jiaofu.domain.PageQuery;

import com.ryqg.jiaofu.business.common.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserPageQuery extends Page {
    private String phone;

    // 用户状态(1-正常 0-停用)
    private Integer status;

    private LocalDateTime[] createTime;

    Boolean isRoot;
}
