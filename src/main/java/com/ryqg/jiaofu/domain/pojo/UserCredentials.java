package com.ryqg.jiaofu.domain.pojo;

import lombok.Data;

import java.util.Set;

@Data
public class UserCredentials {
    private String id;

    private String userName;

    private String phone;

    private String password;

    private Set<String> roles;
}
