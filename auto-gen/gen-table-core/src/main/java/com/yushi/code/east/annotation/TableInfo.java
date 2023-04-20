package com.yushi.code.east.annotation;

import com.yushi.code.east.autoconfig.EastAutoConfiguration;
import com.yushi.code.east.config.EastConfiguration;
import com.yushi.code.east.support.IdGenerator;

import java.lang.annotation.*;

/**
 * 表信息.
 *
 * @author fdong
 * @since 21 /07/2020
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TableInfo {

  /** 表名.指定改值后全局表前缀{@link EastConfiguration#getGlobalTablePrefix()}会失效 */
  String name() default "";

  /** 备注. */
  String comment() default "";

  /** id生成器,默认使用全局配置.该值会覆盖全局配置 */
  Class<? extends IdGenerator> idGenerator() default IdGenerator.VoidIdGenerator.class;
}
