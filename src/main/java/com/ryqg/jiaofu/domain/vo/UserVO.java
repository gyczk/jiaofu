package com.ryqg.jiaofu.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO  extends BaseVO{
    private String id;
    private String userName;
    private String phone;
    private String password;
    private int doc_num;
    private int fans;
    private float coin;
}
