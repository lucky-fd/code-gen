package com.yushi.code.east.jdbc.metadata;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表名与数据库表信息映射.
 *
 * @author fdong
 * @since 2020.08.20
 */
@Data
public class NameTableInformation {
  private Map<String, TableInformation> tables = new HashMap<>();

  public void addTableInformation(TableInformation tableInformation) {
    tables.put(tableInformation.getName(), tableInformation);
  }

  public TableInformation getTableInformation(TableInformation table) {
    return tables.get(table.getName());
  }

  public TableInformation getTableInformation(String tableName) {
    return tables.get(tableName);
  }

  public List<TableInformation> getTableInformations() {
    return new ArrayList<>(tables.values());
  }
}
