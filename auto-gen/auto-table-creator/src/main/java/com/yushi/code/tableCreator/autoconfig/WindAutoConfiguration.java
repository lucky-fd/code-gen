package com.yushi.code.tableCreator.autoconfig;

import com.yushi.code.tableCreator.WindApplication;
import com.yushi.code.tableCreator.config.Configuration;
import com.yushi.code.tableCreator.config.JdbcEnvironment;
import com.yushi.code.tableCreator.config.WindProperty;
import com.yushi.code.tableCreator.jdbc.transaction.SpringManagedTransactionFactory;
import com.yushi.code.tableCreator.jdbc.transaction.TransactionFactory;
import com.yushi.code.tableCreator.support.IdGenerator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * @author ramer
 * @since 2022.06.03
 */
@org.springframework.context.annotation.Configuration
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties(WindProperty.class)
@ConditionalOnProperty(prefix = WindProperty.WIND_PROPERTY_PREFIX, name = "ddl-auto")
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
public class WindAutoConfiguration {
    @Autowired
    private WindProperty windProperty;


    @Bean
    @ConditionalOnMissingBean
    public TransactionFactory transactionFactory() {
        return new SpringManagedTransactionFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    public WindApplication windApplication(
            DataSource dataSource,
            TransactionFactory transactionFactory,
            ObjectProvider<IdGenerator> idGenerator) {
        final Configuration configuration = windProperty.getConfiguration();
        configuration.setJdbcEnvironment(new JdbcEnvironment(transactionFactory, dataSource));
        idGenerator.ifAvailable(configuration::setIdGenerator);
        return WindApplication.run(configuration);
    }
}
