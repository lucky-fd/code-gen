package com.yushi.code.east.support;

import com.yushi.code.east.annotation.TableIndex;
import com.yushi.code.east.dialect.Dialect;
import com.yushi.code.east.domain.Sort;
import com.yushi.code.east.exception.SimpleException;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 表索引信息.
 *
 * @author fdong
 * @since 07/12/2020
 */
@Data
public class EntityIndex {
  private String tableName;
  /** 名称.{@link Index#name()} */
  private String name;
  /** 是否唯一. */
  private boolean unique;
  /** 备注. */
  private String comment;

  private IndexColumn[] indexColumns;

  @AllArgsConstructor(staticName = "of")
  private static class IndexColumn {
    /** 索引列. */
    private String column;
    /** 索引长度. */
    private int length;
    /** 索引排序. */
    private Sort.Direction direction;
  }

  public static List<EntityIndex> getEntityIndexes(
      @Nonnull final Class<?> clazz,
      final String tableName,
      Set<EntityColumn> entityColumns,
      Dialect dialect) {
    final TableIndex tableIndex = clazz.getAnnotation(TableIndex.class);
    if (tableIndex == null) {
      return Collections.emptyList();
    }
    final TableIndex.Index[] indexes = tableIndex.value();
    List<EntityIndex> entityIndexes = new ArrayList<>();
    for (TableIndex.Index index : indexes) {
      TableIndex.IndexField[] indexFields = index.indexFields();
      if (indexFields.length == 0) {
        throw new SimpleException("invalid table index: no fields set");
      }
      final StringBuilder indexNames = new StringBuilder();
      IndexColumn[] indexColumns = new IndexColumn[indexFields.length];
      for (int i = 0; i < indexFields.length; i++) {
        TableIndex.IndexField indexField = indexFields[i];
        final String field = indexField.field();
        Optional<EntityColumn> optional =
            entityColumns.stream().filter(o -> o.getField().getName().equals(field)).findAny();
        if (!optional.isPresent()) {
          throw new SimpleException(
              "No such field found:{" + field + "}, in " + clazz.getTypeName());
        }
        indexColumns[i] =
            IndexColumn.of(optional.get().getName(), indexField.length(), indexField.direction());
        indexNames.append(indexColumns[i].column);
      }
      final EntityIndex entityIndex = new EntityIndex();
      entityIndex.tableName = tableName;
      entityIndex.name =
          !"".equals(index.name())
              ? index.name()
              : "".equals(tableIndex.prefix())
                  ? "idx_" + tableName + "_" + indexNames
                  : tableIndex.prefix() + "_" + indexNames;
      entityIndex.unique = index.unique();
      entityIndex.comment = index.comment();
      entityIndex.indexColumns = indexColumns;
      entityIndexes.add(entityIndex);
    }
    return entityIndexes;
  }

  public String getSqlDefinition(final Dialect dialect) {
    String colums =
        Arrays.stream(indexColumns)
            .map(
                indexColumn ->
                    indexColumn.column
                        + (indexColumn.length == -1 ? " " : "(" + indexColumn.length + ") ")
                        + indexColumn.direction.name())
            .collect(Collectors.joining(","));
    return String.format(
        "create %sindex %s on %s(%s)", unique ? "unique " : "", name, tableName, colums);
  }
}
