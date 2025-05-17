package com.ryqg.jiaofu.config.druid;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;

//@Configuration
public class DruidConfig {

//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }
}