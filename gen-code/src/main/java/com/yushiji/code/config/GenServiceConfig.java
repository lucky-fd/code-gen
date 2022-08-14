package com.yushiji.code.config;

import com.yushiji.code.service.IGenTableColumnService;
import com.yushiji.code.service.IGenTableService;
import com.yushiji.code.service.impl.MysqlGenTableColumnServiceImpl;
import com.yushiji.code.service.impl.MysqlGenTableServiceImpl;
import com.yushiji.code.service.impl.PostgresqlGenTableColumnServiceImpl;
import com.yushiji.code.service.impl.PostgresqlGenTableServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态注入 service接口
 * */
@Configuration
public class GenServiceConfig {

    @Bean
    @ConditionalOnProperty(value = "gen.database-type", havingValue = "mysql")
    public IGenTableService mysqlGenTableService() {
        return new MysqlGenTableServiceImpl();
    }

    @Bean
    @ConditionalOnProperty(value = "gen.database-type", havingValue = "mysql")
    public IGenTableColumnService mysqlGenTableColumnService() {
        return new MysqlGenTableColumnServiceImpl();
    }

    @Bean
    @ConditionalOnProperty(value = "gen.database-type", havingValue = "postgresql")
    public IGenTableService postgresqlGenTableService() {
        return new PostgresqlGenTableServiceImpl();
    }

    @Bean
    @ConditionalOnProperty(value = "gen.database-type", havingValue = "postgresql")
    public IGenTableColumnService postgresqlGenTableColumnService() {
        return new PostgresqlGenTableColumnServiceImpl();
    }
}
