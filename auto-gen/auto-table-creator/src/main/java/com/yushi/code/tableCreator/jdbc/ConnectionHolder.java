package com.yushi.code.tableCreator.jdbc;

import java.sql.Connection;

/**
 * @author fdong
 * @since 2022.03.23
 */
public interface ConnectionHolder {
  void setConnection(final Connection connection);

  Connection getConnection();

  Connection requestConnection();

  void releaseConnection();
}
