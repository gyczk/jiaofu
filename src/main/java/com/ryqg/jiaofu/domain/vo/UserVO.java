package com.ryqg.jiaofu.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO  extends BaseVO{
    private String userName;

    private String phone;

    private int doc_num;

    private int fans;

    private float coin;

    private Integer status;

    private List<String> roleIds;
}
