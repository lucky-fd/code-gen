package com.yushi.code.east.jdbc.dynamicdatasource;

import com.yushi.code.east.jdbc.ConnectionHolder;
import com.yushi.code.east.jdbc.SimpleConnectionHolder;
import com.yushi.code.east.util.DataSourceUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fdong
 * @since 24/02/2022
 */
@Slf4j
public class DynamicConnectionHolder implements ConnectionHolder {
  private final Map<String, SimpleConnectionHolder> dataSourceMap = new ConcurrentHashMap<>();

  public DynamicConnectionHolder(@Nonnull DataSource dataSource) {
    dataSourceMap.put(
        DynamicDataSourceHolder.peek(),
        new SimpleConnectionHolder(DataSourceUtils.getConnection(dataSource)));
  }

  @Override
  public void setConnection(final Connection connection) {
    final String ds = DynamicDataSourceHolder.peek();
    SimpleConnectionHolder connectionHolder = dataSourceMap.get(ds);
    if (connectionHolder == null) {
      connectionHolder = new SimpleConnectionHolder(connection);
      dataSourceMap.put(ds, connectionHolder);
    }
    connectionHolder.setConnection(connection);
  }

  @Override
  public Connection getConnection() {
    final SimpleConnectionHolder connectionHolder =
        dataSourceMap.get(DynamicDataSourceHolder.peek());
    return connectionHolder == null ? null : connectionHolder.getConnection();
  }

  @Override
  public Connection requestConnection() {
    final String ds = DynamicDataSourceHolder.peek();
    final SimpleConnectionHolder holder = dataSourceMap.get(ds);
    if (holder == null) {
      return null;
    }
    return holder.requestConnection();
  }

  @Override
  public void releaseConnection() {
    dataSourceMap.get(DynamicDataSourceHolder.peek()).releaseConnection();
  }
}
