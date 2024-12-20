package com.yushi.code.east.util;

import com.yushi.code.east.exception.DataAccessException;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * @author fdong
 * @since 26/12/2021
 */
@Slf4j
public class JdbcUtils {
  public static String getColumnName(ResultSetMetaData resultSetMetaData, int columnIndex)
      throws SQLException {
    String name = resultSetMetaData.getColumnLabel(columnIndex);
    if (StringUtils.isEmpty(name)) {
      name = resultSetMetaData.getColumnName(columnIndex);
    }
    return name;
  }

  public static void setObject(PreparedStatement ps, final int index, final Object value) {
    try {
      ps.setObject(index, value);
    } catch (SQLException e) {
      DataSourceUtils.close(ps);
      throw new DataAccessException(
          String.format("Fail to set value [index:%s,value:%s]", index, value));
    }
  }

  public static boolean supportsBatchUpdates(Connection con) {
    try {
      DatabaseMetaData dbmd = con.getMetaData();
      if (dbmd != null) {
        if (dbmd.supportsBatchUpdates()) {
          log.debug("JDBC driver supports batch updates");
          return true;
        } else {
          log.debug("JDBC driver does not support batch updates");
        }
      }
    } catch (SQLException ex) {
      log.debug("JDBC driver 'supportsBatchUpdates' method threw exception", ex);
    }
    return false;
  }
}
