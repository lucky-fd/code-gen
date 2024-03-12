package com.yushi.code.east.config;

import com.yushi.code.east.support.IdGenerator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * east配置
 *
 * @author fdong
 * @since 2020/1/14
 */
@Data
@Slf4j
public class EastConfiguration {

    /**
     * 数据库方言全路径.
     */
    protected String dialect;

    /**
     * entity所在包路径,多个以,分割.
     */
    protected String entityPackage = "";

    /**
     * 拦截器所在包路径,多个以,分割.
     */
    protected String interceptorPackage = "";

    /**
     * 类型处理器路径,多个以,分割
     */
    protected String typeHandlerPackage = "";

    /**
     * 表更新模式.
     */
    protected DdlAuto ddlAuto = DdlAuto.NONE;

    /**
     * 新增/更新时写入值为null的属性,默认写入所有字段.
     */
    protected boolean writeNullProp = true;

    /**
     * 全局表前缀.
     */
    private String globalTablePrefix = "";

    /**
     * 指定{@link TimestampStrategy}注解的更新策略,默认总是赋值为当前时间
     */
    protected TimestampStrategy updateTimeStrategy = TimestampStrategy.ALWAYS;

    /**
     * 全局id生成器,默认自增,实体可以单独指定{@link IdGenerator }
     */
    protected IdGenerator idGenerator = IdGenerator.AUTO_INCREMENT_ID_GENERATOR;

    /**
     * jdbc环境配置.数据源,事务
     */
    protected JdbcEnvironment jdbcEnvironment;


    public enum DdlAuto {
        /**
         * Create ddl auto.
         * 每次执行创建操作，表存在则删除再创建
         */
        CREATE,
        /**
         * Update ddl auto.
         * 如果表不存在，则创建表；如果表存在，则更新表（已定义字段不会更新，避免数据库字段类型不一致冲突，只更新新加字段）
         */
        UPDATE,
        /**
         * None ddl auto.
         */
        NONE
    }

    public enum TimestampStrategy {
        /**
         * 总是设置为当前时间
         */
        ALWAYS,
        /**
         * 仅字段为空时设置为当前时间
         */
        NULL
    }
}
