package com.yushi.code.east.annotation;

import java.lang.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 创建时间,当实体创建时,属性值被设置为当前JVM时间.
 *
 * <p>仅支持以下类型
 * <li>{@link LocalDate} 值为:{@link LocalDate#now()}
 * <li>{@link LocalTime} 值为:{@link LocalTime#now()}
 * <li>{@link LocalDateTime} 值为:{@link LocalDateTime#now()}
 * <li>{@link Date} 值为:{@link Date#Date()}
 * <li>{@link Long} 当前时间毫秒数 值为:{@link System#currentTimeMillis()}
 * <li>long 当前时间毫秒数 值为:{@link System#currentTimeMillis()}
 * <li>{@link Integer} 当前时间秒数 值为:{@code System.currentTimeMillis()/1000}
 * <li>int 当前时间秒数 值为:{@code System.currentTimeMillis()/1000}
 *
 * @since 2020.11.23
 * @author fdong
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CreateTimestamp {}
