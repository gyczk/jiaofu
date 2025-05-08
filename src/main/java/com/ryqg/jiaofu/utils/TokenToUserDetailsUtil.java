package com.ryqg.jiaofu.utils;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.ryqg.jiaofu.common.ResultCode;
import com.ryqg.jiaofu.config.security.UserDetailsImpl;
import com.ryqg.jiaofu.common.constants.SecurityConstants;
import com.ryqg.jiaofu.common.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenToUserDetailsUtil {
    public static UserDetailsImpl getUserDetails(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        JSONObject payloads = jwt.getPayloads().getJSONObject(SecurityConstants.LOGIN_INFO);
        try {
            UserDetailsImpl userDetails =
                    com.alibaba.fastjson.JSONObject.parseObject(payloads.toString(), UserDetailsImpl.class);
            if (userDetails == null) {
                throw new com.alibaba.fastjson.JSONException();
            }
            return userDetails;
        } catch (com.alibaba.fastjson.JSONException e){
            throw new InvalidTokenException(ResultCode.ACCESS_TOKEN_INVALID);
        }
    }
}
