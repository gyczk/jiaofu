package com.ryqg.jiaofu.config.security;

import cn.hutool.core.collection.CollectionUtil;
import com.ryqg.jiaofu.business.mapper.UserMapper;
import com.ryqg.jiaofu.common.constants.SecurityConstants;
import com.ryqg.jiaofu.domain.pojo.UserCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * 系统用户认证 DetailsService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    /**
     * 根据用户名获取用户信息
     *
     * @param phone 手机号
     * @return 用户信息
     * @throws UsernameNotFoundException 用户名未找到异常
     */
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        try {
            UserCredentials userCredentials = userMapper.getAuthCredentialsByPhone(phone);
            if (userCredentials == null) {
                throw new UsernameNotFoundException(phone);
            }
            if (CollectionUtil.isNotEmpty(userCredentials.getRoles())){
                userCredentials.setRoles(userCredentials.getRoles().stream()
                        .map(role -> SecurityConstants.ROLE_PREFIX + role).collect(Collectors.toSet()));
            }
            return new UserDetailsImpl(userCredentials);
        } catch (Exception e) {
            // 记录异常日志
            // todo 会抛到前端
            log.error("认证异常:{}", e.getMessage());
            // 抛出异常
            throw e;
        }
    }
}
