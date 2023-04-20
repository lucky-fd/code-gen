package com.yushi.code.east.autoconfig;

import com.yushi.code.east.EastApplication;
import com.yushi.code.east.config.EastConfiguration;
import com.yushi.code.east.config.JdbcEnvironment;
import com.yushi.code.east.config.EastProperty;
import com.yushi.code.east.jdbc.transaction.SpringManagedTransactionFactory;
import com.yushi.code.east.jdbc.transaction.TransactionFactory;
import com.yushi.code.east.support.IdGenerator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author fdong
 * @since 2022.06.03
 */
@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties(EastProperty.class)
@ConditionalOnProperty(prefix = EastProperty.WIND_PROPERTY_PREFIX, name = "ddl-auto")
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
public class EastAutoConfiguration {
    @Autowired
    private EastProperty eastProperty;


    @Bean
    @ConditionalOnMissingBean
    public TransactionFactory transactionFactory() {
        return new SpringManagedTransactionFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public EastApplication eastApplication(
            DataSource dataSource,
            TransactionFactory transactionFactory,
            ObjectProvider<IdGenerator> idGenerator) {
        final EastConfiguration eastConfiguration = eastProperty.getConfiguration();
        // 设置jdbc数据配置
        eastConfiguration.setJdbcEnvironment(new JdbcEnvironment(transactionFactory, dataSource));
        // 设置id生成器
        idGenerator.ifAvailable(eastConfiguration::setIdGenerator);
        return EastApplication.run(eastConfiguration);
    }
}
