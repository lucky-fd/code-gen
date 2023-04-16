package com.yushi.code.tableCreator.autoconfig;

import com.yushi.code.tableCreator.annotation.NestedConfigurationProperties;
import com.yushi.code.tableCreator.config.Configuration;
import com.yushi.code.tableCreator.config.JdbcEnvironment;
import com.yushi.code.tableCreator.exception.WindException;
import com.yushi.code.tableCreator.factory.DataSourceConfigurationFactory;
import com.yushi.code.tableCreator.jdbc.transaction.JdbcTransactionFactory;
import com.yushi.code.tableCreator.jdbc.transaction.TransactionFactory;
import com.yushi.code.tableCreator.support.IdGenerator;
import com.yushi.code.tableCreator.util.BeanUtils;
import com.yushi.code.tableCreator.util.StringUtils;
import com.yushi.code.tableCreator.util.YmlUtil;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 读取文件自动配置{@link Configuration}.
 *
 * @author fdong
 * @since 2022.03.05
 */
@Data
@Slf4j
@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix = "wind1")
public class AutoConfigConfiguration {


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
     * 批量操作时,每次处理的大小.
     */
    private int batchSize = 500;

    /**
     * 表更新模式.
     */
    private Configuration.DdlAuto ddlAuto = Configuration.DdlAuto.NONE;

    /**
     * 新增/更新时写入值为null的属性,默认写入所有字段.
     */
    private boolean writeNullProp = true;

    /**
     * 指定{@link UpdateTimestamp}注解的更新策略,默认总是赋值为当前时间
     */
    protected Configuration.TimestampStrategy updateTimeStrategy = Configuration.TimestampStrategy.ALWAYS;

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

    public Configuration getConfiguration() {
        Configuration configuration = new Configuration();

        configuration.setEntityPackage(entityPackage);
        configuration.setInterceptorPackage(interceptorPackage);
        configuration.setTypeHandlerPackage(typeHandlerPackage);

        configuration.setDdlAuto(ddlAuto);
        configuration.setWriteNullProp(writeNullProp);
        configuration.setUpdateTimeStrategy(updateTimeStrategy);
        if (StringUtils.hasText(idGenerator)) {
            try {
                final IdGenerator idGenerator = BeanUtils.initial(this.idGenerator);
                configuration.setIdGenerator(idGenerator);
            } catch (WindException e) {
                log.error(String.format("Cannot initial idGenerator [%s]", idGenerator), e.getCause());
                throw e;
            }
        }
        if (dataSource != null) {
            configuration.setDialect(dataSource.getDialect());
            configuration.setJdbcEnvironment(
                    new JdbcEnvironment(
                            BeanUtils.initial(dataSource.getTransactionFactory()),
                            DataSourceConfigurationFactory.getDataSource(dataSource)));
        }
        return configuration;
    }
}
