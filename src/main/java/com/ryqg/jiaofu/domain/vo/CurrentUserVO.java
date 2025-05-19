package com.ryqg.jiaofu.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrentUserVO implements Serializable {
    private String id;

    private String userName;

    private String phone;

    private Set<String> roles;

    private Set<String> perms;

}
