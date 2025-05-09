package com.ryqg.jiaofu.config.security;

import com.ryqg.jiaofu.business.mapper.UserMapper;
import com.ryqg.jiaofu.domain.pojo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
            User user = userMapper.getUser(phone);
            if (user == null) {
                throw new UsernameNotFoundException(phone);
            }
            return new UserDetailsImpl(user);
        } catch (Exception e) {
            // 记录异常日志
            log.error("认证异常:{}", e.getMessage());
            // 抛出异常
            throw e;
        }
    }
}
