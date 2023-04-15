package com.yushi.code.tableCreator.jdbc.transaction;


import com.yushi.code.tableCreator.exception.DataAccessException;
import com.yushi.code.tableCreator.jdbc.managed.Transaction;
import com.yushi.code.tableCreator.util.DataSourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;


import javax.sql.DataSource;
import java.sql.Connection;

import static org.springframework.util.Assert.notNull;

/**
 * {@code SpringManagedTransaction} handles the lifecycle of a JDBC connection. It retrieves a
 * connection from Spring's transaction manager and returns it back to it when it is no longer
 * needed.
 *
 * <p>If Spring's transaction handling is active it will no-op all commit/rollback/close calls
 * assuming that the Spring transaction manager will do the job.
 *
 * <p>If it is not it will behave like {@code JdbcTransaction}.
 *
 * @author Hunter Presnall
 * @author Eduardo Macarron
 */
@Slf4j
public class SpringManagedTransaction implements Transaction {

  private final DataSource dataSource;

  private Connection connection;

  private boolean isConnectionTransactional;

  private boolean autoCommit;

  public SpringManagedTransaction(DataSource dataSource) {
    notNull(dataSource, "No DataSource specified");
    this.dataSource = dataSource;
  }

  /** {@inheritDoc} */
  @Override
  public Connection getConnection() throws DataAccessException {
    if (this.connection == null
        || DataSourceUtils.isClosed(this.connection)) {
      openConnection();
    }
    return this.connection;
  }

  @Override
  public void releaseConnection() {}

  /**
   * Gets a connection from Spring transaction manager and discovers if this {@code Transaction}
   * should manage connection or let it to Spring.
   *
   * <p>It also reads autocommit setting because when using Spring Transaction MyBatis thinks that
   * autocommit is always false and will always call commit/rollback so we need to no-op that calls.
   */
  private void openConnection() throws DataAccessException {
    if (log.isDebugEnabled()) {
      log.debug(Thread.currentThread().getName() + " Opening JDBC Connection");
    }
    this.connection = DataSourceUtils.getConnection(this.dataSource);
    this.autoCommit =
        DataSourceUtils.getAutoCommit(this.connection);
    this.isConnectionTransactional =
            org.springframework.jdbc.datasource.DataSourceUtils.isConnectionTransactional(this.connection, this.dataSource);
    log.debug(
        "JDBC Connection ["
            + this.connection
            + "] will"
            + (this.isConnectionTransactional ? " " : " not ")
            + "be managed by Spring");
  }

  /** {@inheritDoc} */
  @Override
  public void commit() throws DataAccessException {
    if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
      log.debug("Committing JDBC Connection [" + this.connection + "]");
      DataSourceUtils.commit(this.connection);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void rollback() throws DataAccessException {
    if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
      log.debug("Rolling back JDBC Connection [" + this.connection + "]");
      DataSourceUtils.rollback(this.connection);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void close() throws DataAccessException {
    org.springframework.jdbc.datasource.DataSourceUtils.releaseConnection(this.connection, this.dataSource);
  }

  /** {@inheritDoc} */
  @Override
  public Integer getTimeout() throws DataAccessException {
    ConnectionHolder holder =
        (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
    if (holder != null && holder.hasTimeout()) {
      return holder.getTimeToLiveInSeconds();
    }
    return null;
  }
}
