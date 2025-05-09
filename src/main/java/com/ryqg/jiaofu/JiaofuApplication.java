package com.ryqg.jiaofu;

import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = RedisRepositoriesAutoConfiguration.class)//消除Multiple Spring Data modules found日志
@ConfigurationPropertiesScan
//@EnableTransactionManagement //开启事务管理注解模式
public class JiaofuApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiaofuApplication.class, args);
    }

    @Bean
    public IKeyGenerator keyGenerator() {
        return new H2KeyGenerator();
    }
}
