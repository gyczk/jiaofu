package com.ryqg.jiaofu.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends BaseDTO{
    private String userName;

    private String password;

    private String phone;

    private int doc_num;

    private int fans;

    private float coin;

    private Integer status;

    private List<String> roleIds;
}
