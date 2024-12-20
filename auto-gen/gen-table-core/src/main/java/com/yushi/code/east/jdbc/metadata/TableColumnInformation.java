package com.yushi.code.east.jdbc.metadata;

import lombok.Data;

import java.util.Objects;

/**
 * 数据库表字段信息.
 *
 * <p>后面可能会使用到扩展列信息,如: <code>nullable, type</code>等
 *
 * @author fdong
 * @since 2020.08.20
 */
@Data
public class TableColumnInformation {
  private String name;
  private int dataType;
  private String typeName;

  public static TableColumnInformation of(
      final String name, final int dataType, final String typeName) {
    TableColumnInformation columnInformation = new TableColumnInformation();
    columnInformation.setName(name);
    columnInformation.setDataType(dataType);
    columnInformation.setTypeName(typeName);
    return columnInformation;
  }

  /** 根据名称匹配. */
  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final TableColumnInformation that = (TableColumnInformation) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
