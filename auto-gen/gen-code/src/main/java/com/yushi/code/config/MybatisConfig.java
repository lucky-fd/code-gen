package com.yushi.code.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// @MapperScan()只能配置成mapper接口所在的包，不能让他扫描到其他接口，否则会将其注册成bean
@MapperScan(basePackages = "com.yushi.code.mapper")
public class MybatisConfig {
}
