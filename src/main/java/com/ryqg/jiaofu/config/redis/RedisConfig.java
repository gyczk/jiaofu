package com.ryqg.jiaofu.config.redis;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 配置类，Redis
 */
@Configuration
@Slf4j
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        // 将redisTemplate 泛型设置为 <String, Object>
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 连接工厂，不必修改
        redisTemplate.setConnectionFactory(connectionFactory);


        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
        // value值的序列化采用fastJsonRedisSerializer
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        // key的序列化采用StringRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}