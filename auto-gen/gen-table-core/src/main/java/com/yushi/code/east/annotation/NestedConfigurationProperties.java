package com.yushi.code.east.annotation;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.lang.annotation.*;

/** 指定一个{@link ConfigurationProperties}的bean属性为配置类 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NestedConfigurationProperties {}
