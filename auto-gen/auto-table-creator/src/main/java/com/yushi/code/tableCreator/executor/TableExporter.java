package com.yushi.code.tableCreator.executor;

import com.yushi.code.tableCreator.WindContext;
import com.yushi.code.tableCreator.config.Configuration;
import com.yushi.code.tableCreator.config.JdbcEnvironment;
import com.yushi.code.tableCreator.dialect.Dialect;

import com.yushi.code.tableCreator.jdbc.metadata.TableColumnInformation;
import com.yushi.code.tableCreator.jdbc.metadata.TableInformation;
import com.yushi.code.tableCreator.jdbc.transaction.TransactionIsolationLevel;
import com.yushi.code.tableCreator.support.EntityColumn;
import com.yushi.code.tableCreator.support.EntityIndex;
import com.yushi.code.tableCreator.support.EntityInfo;
import com.yushi.code.tableCreator.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 自动建表.
 *
 * @since 2020.10.28
 * @author fdong
 */
@Slf4j
public class TableExporter {
  private Executor executor;
  private final WindContext windContext;
  private final Dialect dialect;
  private static final Map<WindContext, TableExporter> INSTANCE_MAP = new ConcurrentHashMap<>();

  private TableExporter(final WindContext windContext) {
    this.windContext = windContext;
    this.dialect = windContext.getDbMetaData().getDialect();
  }

  public static TableExporter of(final WindContext windContext) {
    TableExporter tableExporter = INSTANCE_MAP.get(windContext);
    synchronized (TableExporter.class) {
      if (tableExporter != null) {
        return tableExporter;
      }
      final Configuration configuration = windContext.getConfiguration();
      final JdbcEnvironment jdbcEnvironment = configuration.getJdbcEnvironment();
      final DataSource dataSource = jdbcEnvironment.getDataSource();

      tableExporter = new TableExporter(windContext);
      tableExporter.executor =
          new SimpleJdbcExecutor(
              configuration,
              jdbcEnvironment
                  .getTransactionFactory()
                  .newTransaction(dataSource, TransactionIsolationLevel.READ_COMMITTED, true));
      INSTANCE_MAP.put(windContext, tableExporter);
      return tableExporter;
    }
  }

  /** 先删除,再创建. */
  public void createTable(@Nonnull final EntityInfo entityInfo, final boolean doDelete) {
    // 删除已存在的表
    if (doDelete) {
      final String dropSql = "drop table if exists " + entityInfo.getName();
      log.info("ddlAuto:drop table[{}]", dropSql);
      try {
        executor.update(dropSql, ps -> {});
      } catch (Exception e) {
        log.warn("Fail to drop table:" + entityInfo.getName() + ",sql:" + dropSql, e);
      }
    }
    // 列信息
    {
      final List<EntityColumn> columns = entityInfo.getEntityColumns();
      StringBuilder sql = new StringBuilder("create table ");
      sql.append(entityInfo.getName()).append("(\n\t");
      final String columnDefinition =
          columns.stream()
              .filter(EntityColumn::isSupported)
              .filter(EntityColumn::nonIgnore)
              .sorted(Comparator.comparing(EntityColumn::isPrimaryKey).reversed())
              .map(column -> column.getColumnDdl(dialect))
              .collect(Collectors.joining(",\n\t"));
      sql.append(columnDefinition).append(",\n\t");
      final List<EntityColumn> keys = entityInfo.getPrimaryKeys();
      sql.append("primary key (")
          .append(keys.stream().map(EntityColumn::getName).collect(Collectors.joining(",")))
          .append(")\n\t)");

      final String entityComment = entityInfo.getComment();
      // 不支持comment on的数据库直接跟comment
      if (!dialect.isSupportCommentOn()
          && dialect.isSupportComment()
          && StringUtils.nonEmpty(entityComment)) {
        sql.append(" comment ").append("'").append(entityComment).append("'");
      }
      // 存储引擎,针对mysql
      sql.append(dialect.getTableTypeString()).append(";\n");
      log.info("createTable:[\n{}\n]", sql);
      try {
        executor.update(sql.toString(), ps -> {});
      } catch (Exception e) {
        log.warn("Fail to create table:" + entityInfo.getName(), e);
      }
      // 列注释
      if (dialect.isSupportCommentOn()) {
        final List<String> columnComments =
            columns.stream()
                .filter(EntityColumn::isSupported)
                .filter(EntityColumn::nonIgnore)
                .filter(column -> StringUtils.nonEmpty(column.getComment()))
                .map(comment -> comment.getCommentOnColumnString(entityInfo.getName(), dialect))
                .collect(Collectors.toList());
        try {
          for (String columnComment : columnComments) {
            executor.update(columnComment, ps -> {});
          }
          if (StringUtils.nonEmpty(entityComment)) {
            final String tableComment =
                dialect.getCommonOnTableString(
                    windContext.getDbMetaData().getCatelog(),
                    windContext.getDbMetaData().getSchema(),
                    entityInfo.getName(),
                    entityComment);
            executor.update(tableComment, ps -> {});
          }
        } catch (Exception e) {
          log.warn("Fail to create table comment:" + entityInfo.getName(), e);
        }
      }
    }
    // 索引
    {
      List<String> sqls = new ArrayList<>();
      final List<EntityIndex> entityIndexes = entityInfo.getEntityIndexes();
      if (!entityIndexes.isEmpty()) {
        for (EntityIndex entityIndex : entityIndexes) {
          String sqlDefinition = entityIndex.getSqlDefinition(dialect);
          sqls.add(sqlDefinition);
        }
        log.info("createTable:index[\n{}\n]", String.join("\n", sqls));
        try {
          executor.batchUpdate(sqls.toArray(new String[0]));
        } catch (Exception e) {
          log.warn("Fail to create index of table:" + entityInfo.getName(), e);
        }
      }
    }
  }

  /** 仅新增列，不支持更新列定义 */
  public void updateTable(@Nonnull final EntityInfo entityInfo) {
    final TableInformation tableInformation =
        windContext.getDbMetaData().getTableInformation(entityInfo.getName());
    if (tableInformation == null) {
      createTable(entityInfo, false);
      return;
    }
    final List<EntityColumn> updateColumns = getUpdatingColumns(entityInfo, tableInformation);
    if (updateColumns.isEmpty()) {
      return;
    }
    StringBuilder sql = new StringBuilder("alter table ");
    sql.append(entityInfo.getName()).append("\n\t");

    final String columnDefinition =
        updateColumns.stream()
            .map(column -> dialect.getAddColumnString() + " " + column.getColumnDdl(dialect))
            .collect(Collectors.joining(",\n\t"));
    sql.append(columnDefinition);
    log.info("updateTable:[\n{}\n]", sql);
    try {
      executor.update(sql.toString(), ps -> {});
    } catch (Exception e) {
      log.warn("Fail to execute ddl of table:" + entityInfo.getName() + ",sql:" + sql, e);
    }
    // 列注释
    if (dialect.isSupportCommentOn()) {
      final List<String> columnComments =
          updateColumns.stream()
              .filter(EntityColumn::isSupported)
              .filter(EntityColumn::nonIgnore)
              .filter(column -> StringUtils.nonEmpty(column.getComment()))
              .map(comment -> comment.getCommentOnColumnString(entityInfo.getName(), dialect))
              .collect(Collectors.toList());
      try {
        for (String columnComment : columnComments) {
          executor.update(columnComment, ps -> {});
        }
      } catch (Exception e) {
        log.warn("Fail to update table comment:" + entityInfo.getName(), e);
      }
    }
  }

  /** 获取需要更新的列. */
  private List<EntityColumn> getUpdatingColumns(
      @Nonnull final EntityInfo entityInfo, final TableInformation tableInformation) {
    final List<TableColumnInformation> existColumns = tableInformation.getColumns();
    return entityInfo.getEntityColumns().stream()
        .filter(
            column ->
                existColumns.stream()
                    .noneMatch(existColumn -> existColumn.getName().equals(column.getName())))
        .filter(EntityColumn::isSupported)
        .filter(EntityColumn::nonIgnore)
        .collect(Collectors.toList());
  }
}