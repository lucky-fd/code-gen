package com.yushi.code.tableCreator;


import com.yushi.code.tableCreator.annotation.TableInfo;
import com.yushi.code.tableCreator.autoconfig.AutoConfigConfiguration;
import com.yushi.code.tableCreator.config.Configuration;
import com.yushi.code.tableCreator.config.JdbcEnvironment;
import com.yushi.code.tableCreator.helper.EntityHelper;
import com.yushi.code.tableCreator.jdbc.metadata.DbMetaData;
import com.yushi.code.tableCreator.jdbc.transaction.JdbcTransactionFactory;
import com.yushi.code.tableCreator.jdbc.transaction.TransactionFactory;
import com.yushi.code.tableCreator.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * The type Wind application.
 *
 * @since 2022.03.19
 * @author fdong
 */
@Slf4j
public class WindApplication {
  private WindContext windContext = new WindContext();

  private WindApplication() {}

  /** 通过指定配置文件启动. */
  public static WindApplication run(final InputStream inputStream) {
    final AutoConfigConfiguration autoConfigConfiguration =
        YmlUtil.process(AutoConfigConfiguration.class, inputStream);
    return run(autoConfigConfiguration.getConfiguration());
  }

  /** 通过指定配置文件启动. */
  public static WindApplication run(final String configPath) {
    final AutoConfigConfiguration autoConfigConfiguration =
        YmlUtil.process(AutoConfigConfiguration.class, configPath);
    return run(autoConfigConfiguration.getConfiguration());
  }

  public static WindApplication run(@Nonnull final DataSource dataSource) {
    return run(new JdbcTransactionFactory(), dataSource);
  }

  public static WindApplication run(
          @Nonnull final TransactionFactory transactionFactory, @Nonnull final DataSource dataSource) {
    Configuration configuration = new Configuration();
    configuration.setJdbcEnvironment(new JdbcEnvironment(transactionFactory, dataSource));
    return run(configuration);
  }

  public static WindApplication run(@Nonnull Configuration configuration) {
    final JdbcEnvironment jdbcEnvironment = configuration.getJdbcEnvironment();
    Asserts.notNull(jdbcEnvironment, "DataSource not found");
    final DataSource dataSource = jdbcEnvironment.getDataSource();
    WindContext windContext = new WindContext();
    windContext.setDbMetaData(DbMetaData.getInstance(dataSource, configuration.getDialect()));
    windContext.setConfiguration(configuration);
    // 打印banner
    printBanner();
    // 初始化实体解析类
    EntityUtils.initial(windContext);
    EntityHelper.initial(windContext);
    // 解析实体元数据
    initEntityInfo(configuration);
    WindApplication windApplication = new WindApplication();
    windApplication.windContext = windContext;
    return windApplication;
  }

  public WindContext getWindContext() {
    return windContext;
  }

  public Configuration getConfiguration() {
    return windContext.getConfiguration();
  }

  private static void printBanner() {
    System.out.println(
        "\n"
            + " _       __    ____    _   __    ____ \n"
            + "| |     / /   /  _/   / | / /   / __ \\\n"
            + "| | /| / /    / /    /  |/ /   / / / /\n"
            + "| |/ |/ /   _/ /_   / /|  /   / /_/ /\n"
            + "|__/|__/   /___/   /_/ |_/   /_____/");
    System.out.println(
        AnsiOutput.toString(
            AnsiColor.GREEN,
            " :: wind ::",
            AnsiColor.DEFAULT,
            " (v" + WindVersion.getVersion() + ")\n",
            AnsiStyle.FAINT));
  }

  private static void initEntityInfo(final Configuration configuration) {
    String entityPackage;
    if (StringUtils.nonEmpty(configuration.getEntityPackage())) {
      entityPackage = configuration.getEntityPackage();
    } else {
      entityPackage = WindVersion.class.getPackage().getName();
    }
    log.info("initEntityInfo:package[{}]", entityPackage);
    Set<Class<?>> entities;
    try {
      entities = BeanUtils.scanClassesWithAnnotation(entityPackage, TableInfo.class);
      if (entities.size() < 1) {
        log.info(
            "no entity with @TableInfo annotation found in path: [{}], correct your configuration:wind.entity-package",
            entityPackage);
      }
    } catch (IOException e) {
      log.warn("Fail to init entity info:" + entityPackage, e);
      return;
    }
    if (!entities.isEmpty()) {
      entities.forEach(EntityHelper::initEntity);
      EntityHelper.initEntityMapping();
    }
  }
}
