package com.yushi.code.config;

import com.yushi.code.common.core.enums.DatabaseType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 代码生成相关配置
 *
 * @author lucky_fd
 * @since 2020.6.8
 */
@Component
@ConfigurationProperties(prefix = "gen")
public class GenConfig {
    /**
     * 数据库类型
     */
    public DatabaseType databaseType;

    /**
     * 作者
     */
    public String author;

    /**
     * 生成包路径
     */
    public String packageName;

    /**
     * 自动去除表前缀，默认是false
     */
    public boolean autoRemovePre;

    /**
     * 表前缀(类名不会包含表前缀)
     */
    public String tablePrefix;

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isAutoRemovePre() {
        return autoRemovePre;
    }

    public void setAutoRemovePre(boolean autoRemovePre) {
        this.autoRemovePre = autoRemovePre;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }
}
