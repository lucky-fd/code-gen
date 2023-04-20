package com.yushi.code.east.handler;

import com.yushi.code.east.util.JdbcUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @since 2021.12.26
 * @author fdong
 */
@Slf4j
public class MapResultHandler extends AbstractResultHandler<Map<String, Object>> {

  @Override
  public Map<String, Object> handle(final ResultSet rs) throws SQLException {
    Map<String, Object> map = new LinkedHashMap<>();
    ResultSetMetaData rsmd = rs.getMetaData();
    int columnCount = rsmd.getColumnCount();
    for (int index = 1; index <= columnCount; index++) {
      final String column = JdbcUtils.getColumnName(rsmd, index);
      Object value = rs.getObject(index);
      map.put(column, value);
    }
    return map;
  }
}
