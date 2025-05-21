package com.ryqg.jiaofu.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ryqg.jiaofu.business.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_users")
public class User extends BaseModel {
    private String id;

    private String userName;

    private String phone;

    private String password;

    private int doc_num;

    private int fans;

    private float coin;

    // (1:启用；0:禁用)
    private Integer status;
}
