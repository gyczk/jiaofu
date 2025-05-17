package com.ryqg.jiaofu.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenUserVO {
    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * phone
     */
    private String phone;

    /**
     * 角色权限集合
     */
    private Set<String> roles;

}
