package com.yushi.code.east;


import com.yushi.code.east.annotation.TableInfo;
import com.yushi.code.east.autoconfig.AutoConfiguration;
import com.yushi.code.east.config.EastConfiguration;
import com.yushi.code.east.config.JdbcEnvironment;
import com.yushi.code.east.helper.EntityHelper;
import com.yushi.code.east.jdbc.metadata.DbMetaData;
import com.yushi.code.east.jdbc.transaction.JdbcTransactionFactory;
import com.yushi.code.east.jdbc.transaction.TransactionFactory;
import com.yushi.code.east.util.*;
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
 * The type east application.
 *
 * @author fdong
 * @since 2022.03.19
 */
@Slf4j
public class EastApplication {
    private EastContext eastContext;

    private EastApplication() {
    }

    /**
     * 通过指定配置文件启动.
     */
    public static EastApplication run(final InputStream inputStream) {
        final AutoConfiguration autoConfiguration =
                YmlUtil.process(AutoConfiguration.class, inputStream);
        return run(autoConfiguration.getConfiguration());
    }

    /**
     * 通过指定配置文件启动.
     */
    public static EastApplication run(final String configPath) {
        final AutoConfiguration autoConfiguration =
                YmlUtil.process(AutoConfiguration.class, configPath);
        return run(autoConfiguration.getConfiguration());
    }

    public static EastApplication run(@Nonnull final DataSource dataSource) {
        return run(new JdbcTransactionFactory(), dataSource);
    }

    public static EastApplication run(
            @Nonnull final TransactionFactory transactionFactory, @Nonnull final DataSource dataSource) {
        EastConfiguration eastConfiguration = new EastConfiguration();
        eastConfiguration.setJdbcEnvironment(new JdbcEnvironment(transactionFactory, dataSource));
        return run(eastConfiguration);
    }

    public static EastApplication run(@Nonnull EastConfiguration eastConfiguration) {
        final JdbcEnvironment jdbcEnvironment = eastConfiguration.getJdbcEnvironment();
        Asserts.notNull(jdbcEnvironment, "DataSource not found");
        final DataSource dataSource = jdbcEnvironment.getDataSource();
        EastContext eastContext = new EastContext();
        eastContext.setDbMetaData(DbMetaData.getInstance(dataSource, eastConfiguration.getDialect()));
        eastContext.setEastConfiguration(eastConfiguration);
        // 打印banner
        printBanner();
        // 初始化实体解析类
        EntityUtils.initial(eastContext);
        EntityHelper.initial(eastContext);
        // 解析实体元数据
        initEntityInfo(eastConfiguration);
        EastApplication eastApplication = new EastApplication();
        eastApplication.eastContext = eastContext;
        return eastApplication;
    }

    private static void printBanner() {
        System.out.println(
                "\n"
                        + " _       __    ____    _   __    ____ \n"
                        + "| |     / /   /  _/   / | / /   / __ \\\n"
                        + "| | /| / /    / /    /  |/ /   / / / /\n"
                        + "| |/ |/ /   _/ /_   / /|  /   / /_/ /\n"
                        + "|__/|__/   /___/   /_/ |_/   /_____/");

        System.out.println("\n" +
                "   ___  __ _ ___| |_ \n" +
                "  / _ \\/ _` / __| __|\n" +
                " |  __/ (_| \\__ \\ |_ \n" +
                "  \\___|\\__,_|___/\\__|");
        System.out.println(
                AnsiOutput.toString(
                        AnsiColor.GREEN,
                        " :: east ::",
                        AnsiColor.DEFAULT,
                        " (v" + EastVersion.getVersion() + ")\n",
                        AnsiStyle.FAINT));
    }

    /**
     * 初始化实体信息
     */
    private static void initEntityInfo(final EastConfiguration eastConfiguration) {
        String entityPackage;
        if (StringUtils.nonEmpty(eastConfiguration.getEntityPackage())) {
            entityPackage = eastConfiguration.getEntityPackage();
        } else {
            entityPackage = EastVersion.class.getPackage().getName();
        }
        log.info("initEntityInfo:package[{}]", entityPackage);
        Set<Class<?>> entities;
        try {
            entities = BeanUtils.scanClassesWithAnnotation(entityPackage, TableInfo.class);
            if (entities.size() < 1) {
                log.info(
                        "no entity with @TableInfo annotation found in path: [{}], correct your configuration:east.entity-package",
                        entityPackage);
            }
        } catch (IOException e) {
            log.error("Fail to init entity info:" + entityPackage, e);
            return;
        }
        if (!entities.isEmpty()) {
            entities.forEach(EntityHelper::initEntity);
            EntityHelper.initEntityMapping();
        }
    }
}
