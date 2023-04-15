package com.yushi.code.tableCreator.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import com.yushi.code.tableCreator.config.Configuration.DdlAuto;
import com.yushi.code.tableCreator.config.Configuration.TimestampStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ramer
 * @since 2022.06.03
 */
@Data
@Slf4j
@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix = WindProperty.WIND_PROPERTY_PREFIX)
public class WindProperty {
    public static final String WIND_PROPERTY_PREFIX = "wind";

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

    public Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setDdlAuto(ddlAuto);
        configuration.setEntityPackage(entityPackage);
        configuration.setWriteNullProp(writeNullProp);
        configuration.setUpdateTimeStrategy(updateTimeStrategy);
        return configuration;
    }
}
