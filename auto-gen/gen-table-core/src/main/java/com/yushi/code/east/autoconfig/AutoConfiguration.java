package com.yushi.code.east.autoconfig;

import com.yushi.code.east.annotation.NestedConfigurationProperties;
import com.yushi.code.east.annotation.UpdateTimestamp;
import com.yushi.code.east.config.EastConfiguration;
import com.yushi.code.east.config.JdbcEnvironment;
import com.yushi.code.east.exception.WindException;
import com.yushi.code.east.factory.DataSourceConfigurationFactory;
import com.yushi.code.east.jdbc.transaction.JdbcTransactionFactory;
import com.yushi.code.east.jdbc.transaction.TransactionFactory;
import com.yushi.code.east.support.IdGenerator;
import com.yushi.code.east.util.BeanUtils;
import com.yushi.code.east.util.StringUtils;
import com.yushi.code.east.util.YmlUtil;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 读取文件自动配置{@link EastConfiguration}.
 *
 * @author fdong
 * @since 2022.03.05
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "wind")
public class AutoConfiguration {


    /**
     * entity所在包路径,多个以,分割.<br>
     */
    private String entityPackage = "";

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
    private EastConfiguration.DdlAuto ddlAuto = EastConfiguration.DdlAuto.NONE;

    /**
     * 新增/更新时写入值为null的属性,默认写入所有字段.
     */
    private boolean writeNullProp = true;

    /**
     * 指定{@link UpdateTimestamp}注解的更新策略,默认总是赋值为当前时间
     */
    protected EastConfiguration.TimestampStrategy updateTimeStrategy = EastConfiguration.TimestampStrategy.ALWAYS;

    /**
     * 全局id生成器,类全路径,默认自增 {@link IdGenerator#AUTO_INCREMENT_ID_GENERATOR }
     */
    private String idGenerator;

    @NestedConfigurationProperties
    private DataSourceConfig dataSource;

    @Data
    public static class DataSourceConfig {
        /**
         * 事务工厂.
         */
        private Class<? extends TransactionFactory> transactionFactory = JdbcTransactionFactory.class;

        /**
         * 数据库方言全路径.
         */
        private String dialect;

        /**
         * 定义数据源属性,根据不同的数据源使用不同的属性
         */
        @Getter
        private Map<String, String> properties;

        /**
         * 默认支持的数据源.
         */
        public enum DataSourceType {
            DBCP,
            HIKARI,
            DRUID,
            OTHER;
        }

        @YmlUtil.After
        public void after(YmlUtil.YmlAfter ymlAfter) {
            this.properties = ymlAfter.getProperties();
        }
    }

    public EastConfiguration getConfiguration() {
        EastConfiguration eastConfiguration = new EastConfiguration();

        eastConfiguration.setEntityPackage(entityPackage);
        eastConfiguration.setInterceptorPackage(interceptorPackage);
        eastConfiguration.setTypeHandlerPackage(typeHandlerPackage);

        eastConfiguration.setDdlAuto(ddlAuto);
        eastConfiguration.setWriteNullProp(writeNullProp);
        eastConfiguration.setUpdateTimeStrategy(updateTimeStrategy);
        if (StringUtils.hasText(idGenerator)) {
            try {
                final IdGenerator idGenerator = BeanUtils.initial(this.idGenerator);
                eastConfiguration.setIdGenerator(idGenerator);
            } catch (WindException e) {
                log.error(String.format("Cannot initial idGenerator [%s]", idGenerator), e.getCause());
                throw e;
            }
        }
        if (dataSource != null) {
            eastConfiguration.setDialect(dataSource.getDialect());
            eastConfiguration.setJdbcEnvironment(
                    new JdbcEnvironment(
                            BeanUtils.initial(dataSource.getTransactionFactory()),
                            DataSourceConfigurationFactory.getDataSource(dataSource)));
        }
        return eastConfiguration;
    }
}
