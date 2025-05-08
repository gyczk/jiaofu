package com.ryqg.jiaofu.config.security;


import cn.hutool.core.util.ArrayUtil;
import com.ryqg.jiaofu.config.property.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 配置类
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // 自定义的用于认证的过滤器，进行jwt的校验操作
    private final JwtTokenOncePerRequestFilter jwtTokenFilter;

    // 认证用户无权限访问资源的处理器
    private final CustomerAccessDeniedHandler customerAccessDeniedHandler;

    // 客户端进行认证数据的提交时出现异常，或者是匿名用户访问受限资源的处理器
    private final AnonymousAuthenticationHandler anonymousAuthentication;

    // 用户认证校验失败处理器
    private final LoginFailureHandler loginFailureHandler;

    private final SecurityProperties securityProperties;

    /**
     * 配置安全过滤链 SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.formLogin(conf -> conf.failureHandler(loginFailureHandler))
                .authorizeHttpRequests(requestMatcherRegistry -> {
                            // 配置无需登录即可访问的公开接口
                            String[] ignoreUrls = securityProperties.getIgnoreUrls();
                            if (ArrayUtil.isNotEmpty(ignoreUrls)) {
                                requestMatcherRegistry.requestMatchers(ignoreUrls).permitAll();
                            }
                            // 其他所有请求需登录后访问
                            requestMatcherRegistry.anyRequest().authenticated();
                        }
                )
                .exceptionHandling(configurer ->
                        configurer
                                .authenticationEntryPoint(anonymousAuthentication) // 未认证异常处理器
                                .accessDeniedHandler(customerAccessDeniedHandler) // 无权限访问异常处理器
                )

                // 禁用默认的 Spring Security 特性，适用于前后端分离架构
                .sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 无状态认证，不使用 Session
                )
                .csrf(AbstractHttpConfigurer::disable)      // 禁用 CSRF 防护，前后端分离无需此防护机制
                .formLogin(AbstractHttpConfigurer::disable) // 禁用默认的表单登录功能，前后端分离采用 Token 认证方式

                .httpBasic(AbstractHttpConfigurer::disable) // 禁用 HTTP Basic 认证，避免弹窗式登录
                // 禁用 X-Frame-Options 响应头，允许页面被嵌套到 iframe 中
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // 限流过滤器
//                .addFilterBefore(new RateLimiterFilter(redisTemplate, configService), UsernamePasswordAuthenticationFilter.class)
                // 验证码校验过滤器
//                .addFilterBefore(new CaptchaValidationFilter(redisTemplate, codeGenerator), UsernamePasswordAuthenticationFilter.class)
                // 验证和解析过滤器
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * 配置Web安全自定义器，以忽略特定请求路径的安全性检查。
     * <p>
     * 该配置用于指定哪些请求路径不经过Spring Security过滤器链。通常用于静态资源文件。
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            String[] unsecuredUrls = securityProperties.getUnsecuredUrls();
            if (ArrayUtil.isNotEmpty(unsecuredUrls)) {
                web.ignoring().requestMatchers(unsecuredUrls);
            }
        };
    }

    /**
     * 登录时调用AuthenticationManager.authenticate执行一次校验
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("czk"));
    }
}
