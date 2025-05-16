package com.ryqg.jiaofu.domain.dto;

import lombok.Data;

@Data
public class UserRegisterDTO extends BaseDTO{
    private String userName;

    private String password;

    private String phone;
}
