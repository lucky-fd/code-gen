package com.yushi.code.east.jdbc;

import com.yushi.code.east.util.DataSourceUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.sql.Connection;

/**
 * @author fdong
 * @since 24/02/2022
 */
@Slf4j
public class SimpleConnectionHolder implements ConnectionHolder {
  private Connection connection;
  @Getter @Setter private int referenceCount;

  public SimpleConnectionHolder(@Nonnull Connection connection) {
    this.connection = connection;
  }

  @Override
  public void setConnection(final Connection connection) {
    this.connection = connection;
  }

  @Override
  public Connection getConnection() {
    return connection;
  }

  @Override
  public Connection requestConnection() {
    this.referenceCount++;
    return connection;
  }

  @Override
  public void releaseConnection() {
    if (log.isDebugEnabled()) {
      log.debug(Thread.currentThread().getName() + " release JDBC Connection [" + connection + "]");
    }
    this.referenceCount--;
    if (referenceCount < 0) {
      DataSourceUtils.close(connection);
      this.connection = null;
      this.referenceCount = 0;
    }
  }
}
