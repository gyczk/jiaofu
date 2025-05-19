package com.ryqg.jiaofu.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class UserCredentials implements Serializable {
    private String id;

    private String userName;

    private String phone;

    private String password;

    private Set<String> roles;
}
