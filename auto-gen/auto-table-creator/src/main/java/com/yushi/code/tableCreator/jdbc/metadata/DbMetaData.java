package com.yushi.code.tableCreator.jdbc.metadata;


import com.yushi.code.tableCreator.dialect.Dialect;
import com.yushi.code.tableCreator.util.DataSourceUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Collection;


/**
 * 数据库元数据.
 *
 * @author fdong
 * @since 2020.08.20
 */
@Slf4j
public class DbMetaData {
  private static volatile DbMetaData INSTANCE;
  @Getter private final Dialect dialect;
  @Getter private final String catelog;
  @Getter private final String schema;

  private final NameTableInformation tableInformations;

  private DbMetaData(DataSource dataSource, final String dialectName) {
    Connection connection = DbResolver.getConnection(dataSource);
    final DatabaseMetaData databaseMetaData = DbResolver.getMetaData(connection);
    final String catalog = DbResolver.getCatalog(connection);
    final String schema = DbResolver.getSchema(connection);
    this.catelog = catalog;
    this.schema = schema;
    this.tableInformations = DbResolver.getTables(databaseMetaData, catalog, schema);
    this.dialect =
        dialectName != null ? Dialect.getInstance(dialectName) : Dialect.getInstance(dataSource);
    DataSourceUtils.releaseConnection(connection);
  }

  /** 该方法返回一个单例. */
  public static DbMetaData getInstance(DataSource dataSource, final String dialect) {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    synchronized (DbMetaData.class) {
      if (INSTANCE != null) {
        return INSTANCE;
      }
      return INSTANCE = new DbMetaData(dataSource, dialect);
    }
  }

  /** 获取所有表信息. */
  public Collection<TableInformation> getTableInformations() {
    return this.tableInformations.getTables().values();
  }

  /** 获取表信息. */
  public TableInformation getTableInformation(final String tableName) {
    return this.tableInformations.getTables().get(tableName);
  }
}
