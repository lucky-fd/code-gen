package com.yushi.code.tableCreator.config;

import com.sun.xml.internal.ws.api.client.ServiceInterceptor;
import com.yushi.code.tableCreator.support.IdGenerator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * wind配置.
 *
 * @since 2020/1/14
 * @author ramer
 */
@Data
@Slf4j
public class Configuration {

  /** entity所在包路径,多个以,分割. */
  protected String entityPackage = "";

  /** 拦截器所在包路径,多个以,分割. */
  protected String interceptorPackage = "";

  /** 类型处理器路径,多个以,分割 */
  protected String typeHandlerPackage = "";

  /** 表更新模式. */
  protected DdlAuto ddlAuto = DdlAuto.NONE;

  /** 数据库方言全路径. */
  protected String dialect;

  /** 新增/更新时写入值为null的属性,默认写入所有字段. */
  protected boolean writeNullProp = true;

  /** 全局表前缀. */
  private String globalTablePrefix = "";

  /** 指定{@link TimestampStrategy}注解的更新策略,默认总是赋值为当前时间 */
  protected TimestampStrategy updateTimeStrategy = TimestampStrategy.ALWAYS;

  /** 全局id生成器,默认自增,实体可以单独指定{@link IdGenerator } */
  protected IdGenerator idGenerator = IdGenerator.AUTO_INCREMENT_ID_GENERATOR;

  /** jdbc环境配置.数据源,事务 */
  protected JdbcEnvironment jdbcEnvironment;


  public enum DdlAuto {
    /** Create ddl auto. */
    CREATE,
    /** Update ddl auto. */
    UPDATE,
    /** None ddl auto. */
    NONE
  }

  public enum TimestampStrategy {
    /** 总是设置为当前时间 */
    ALWAYS,
    /** 仅字段为空时设置为当前时间 */
    NULL
  }
}
