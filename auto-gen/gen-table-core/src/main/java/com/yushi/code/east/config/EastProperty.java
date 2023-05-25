package com.yushi.code.east.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import com.yushi.code.east.config.EastConfiguration.DdlAuto;
import com.yushi.code.east.config.EastConfiguration.TimestampStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * east配置类
 *
 * @author fdong
 * @since 2022.06.03
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = EastProperty.EAST_PROPERTY_PREFIX)
public class EastProperty {
    public static final String EAST_PROPERTY_PREFIX = "east";

    /**
     * entity所在包路径,多个以,分割.<br>
     */
    private String entityPackage = "";

    /**
     * 新增/更新时写入值为null的属性,默认写入所有字段.
     */
    private boolean writeNullProp = true;

    /**
     * 全局表前缀.
     */
    private String globalTablePrefix = "";

    /**
     * 表更新模式.
     */
    private DdlAuto ddlAuto = DdlAuto.NONE;

    /**
     * 指定{@link TimestampStrategy}注解的更新策略,默认总是赋值为当前时间
     */
    protected TimestampStrategy updateTimeStrategy = TimestampStrategy.ALWAYS;

    public EastConfiguration getConfiguration() {
        EastConfiguration eastConfiguration = new EastConfiguration();
        eastConfiguration.setDdlAuto(ddlAuto);
        eastConfiguration.setEntityPackage(entityPackage);
        eastConfiguration.setWriteNullProp(writeNullProp);
        eastConfiguration.setUpdateTimeStrategy(updateTimeStrategy);
        eastConfiguration.setGlobalTablePrefix(globalTablePrefix);
        return eastConfiguration;
    }
}
