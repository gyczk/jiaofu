package com.ryqg.jiaofu.pojo;

import lombok.Data;

@Data
public class User {
    private String userId;
    private String userName;
    private String phone;
    private String password;
    private int doc_num;
    private int fans;
    private float coin;
}
