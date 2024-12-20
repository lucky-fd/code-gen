package com.yushi.code.east.jdbc.transaction;

import com.yushi.code.east.exception.DataAccessException;
import com.yushi.code.east.exception.TransactionException;
import com.yushi.code.east.jdbc.ConnectionHolder;
import com.yushi.code.east.jdbc.TransactionSynchronizationManager;
import com.yushi.code.east.jdbc.managed.Transaction;
import com.yushi.code.east.util.DataSourceUtils;

import lombok.extern.slf4j.Slf4j;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class JdbcTransaction implements Transaction {
  protected DataSource dataSource;
  protected Connection connection;
  protected ConnectionHolder connectionHolder;
  protected TransactionIsolationLevel level;
  protected boolean autoCommit;

  public JdbcTransaction(final Connection connection) {
    this.connection = connection;
  }

  public JdbcTransaction(
      DataSource dataSource, TransactionIsolationLevel desiredLevel, boolean autoCommit) {
    this.dataSource = dataSource;
    this.level = desiredLevel;
    this.autoCommit = autoCommit;
  }

  @Override
  public Connection getConnection() throws DataAccessException {
    if (this.connection == null) {
      this.openConnection();
      return this.connectionHolder.getConnection();
    }
    if (this.connectionHolder == null && !DataSourceUtils.isClosed(this.connection)) {
      return this.connection;
    }
    final Connection currentConnection = this.connectionHolder.requestConnection();
    if (currentConnection == null) {
      this.openConnection();
      return this.connectionHolder.getConnection();
    }
    return currentConnection;
  }

  protected void openConnection() throws DataAccessException {
    if (log.isDebugEnabled()) {
      log.debug(Thread.currentThread().getName() + " Opening JDBC Connection");
    }

    this.connectionHolder = TransactionSynchronizationManager.getConnectionHolder(this.dataSource);
    this.connection = connectionHolder.getConnection();
    if (this.level != null) {
      try {
        //noinspection MagicConstant
        this.connection.setTransactionIsolation(this.level.getLevel());
      } catch (SQLException e) {
        log.warn("The Connection not support to set TransactionIsolation", e);
      }
    }
    this.setAutoCommit(this.autoCommit);
  }

  @Override
  public void releaseConnection() {
    if (log.isDebugEnabled()) {
      log.debug(
          Thread.currentThread().getName() + " Release JDBC Connection [" + this.connection + "]");
    }
    TransactionSynchronizationManager.releaseConnection(this.connection, this.dataSource);
  }

  @Override
  public void commit() throws DataAccessException {
    if (this.connection != null && !DataSourceUtils.getAutoCommit(this.connection)) {
      if (log.isDebugEnabled()) {
        log.debug(
            Thread.currentThread().getName() + " Commit JDBC Connection [" + this.connection + "]");
      }
      DataSourceUtils.commit(this.connection);
    }
  }

  @Override
  public void rollback() throws DataAccessException {
    if (this.connection != null && !DataSourceUtils.getAutoCommit(this.connection)) {
      if (log.isDebugEnabled()) {
        log.debug(
            Thread.currentThread().getName()
                + " Rolling back JDBC Connection ["
                + this.connection
                + "]");
      }
      DataSourceUtils.rollback(this.connection);
    }
  }

  @Override
  public void close() throws DataAccessException {
    if (this.connection != null) {
      this.resetAutoCommit();
      if (log.isDebugEnabled()) {
        log.debug(
            Thread.currentThread().getName()
                + " Closing JDBC Connection ["
                + this.connection
                + "]");
      }
      DataSourceUtils.close(this.connection);
    }
  }

  protected void setAutoCommit(boolean desiredAutoCommit) {
    try {
      if (this.connection.getAutoCommit() != desiredAutoCommit) {
        if (log.isDebugEnabled()) {
          log.debug(
              Thread.currentThread().getName()
                  + " Setting autocommit to "
                  + desiredAutoCommit
                  + " on JDBC Connection ["
                  + this.connection
                  + "]");
        }
        this.connection.setAutoCommit(desiredAutoCommit);
      }
    } catch (SQLException e) {
      throw new TransactionException(
          "Error configuring AutoCommit.  Your driver may not support getAutoCommit() or setAutoCommit(). Requested setting: "
              + desiredAutoCommit
              + ".  Cause: "
              + e,
          e);
    }
  }

  protected void resetAutoCommit() {
    try {
      if (!this.connection.getAutoCommit()) {
        if (log.isDebugEnabled()) {
          log.debug("Resetting autocommit to true on JDBC Connection [" + this.connection + "]");
        }
        this.connection.setAutoCommit(true);
      }
    } catch (SQLException e) {
      if (log.isDebugEnabled()) {
        log.debug("Error resetting autocommit to true before closing the connection.  Cause: " + e);
      }
    }
  }

  @Override
  public Integer getTimeout() throws DataAccessException {
    return null;
  }
}
