package com.ryqg.jiaofu.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends BaseDTO{
    private String id;

    private String userName;

    private String password;

    private String phone;

    private int doc_num;

    private int fans;

    private float coin;
}
