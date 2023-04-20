package com.yushi.code.east.executor;

import com.yushi.code.east.config.EastConfiguration;
import com.yushi.code.east.exception.DataAccessException;
import com.yushi.code.east.executor.logging.Log;
import com.yushi.code.east.executor.logging.SimpleLog;
import com.yushi.code.east.jdbc.managed.Transaction;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * The type Base executor.
 *
 * @since 2022.02.20
 * @author fdong
 */
public abstract class BaseExecutor implements Executor {
  protected Transaction transaction;
  protected Executor wrapper;
  protected EastConfiguration eastConfiguration;
  private boolean closed;
  protected Log log;

  protected BaseExecutor(EastConfiguration eastConfiguration, Transaction transaction) {
    this(eastConfiguration, transaction, new SimpleLog(BaseExecutor.class));
  }

  protected BaseExecutor(EastConfiguration eastConfiguration, Transaction transaction, final Log log) {
    this.transaction = transaction;
    this.closed = false;
    this.eastConfiguration = eastConfiguration;
    this.wrapper = this;
    this.log = log;
  }

  @Override
  public Transaction getTransaction() {
    if (closed) {
      throw new DataAccessException("Executor was closed.");
    }
    return transaction;
  }

  @Override
  public void close(boolean forceRollback) {
    try {
      try {
        rollback(forceRollback);
      } finally {
        if (transaction != null) {
          transaction.close();
        }
      }
    } catch (DataAccessException e) {
      // Ignore. There's nothing that can be done at this point.
      log.warn("Unexpected exception on closing transaction.  Cause: " + e);
    } finally {
      transaction = null;
      closed = true;
    }
  }

  @Override
  public boolean isClosed() {
    return closed;
  }

  @Override
  public void commit(boolean required) throws DataAccessException {
    if (closed) {
      throw new DataAccessException("Cannot commit, transaction is already closed");
    }
    if (required) {
      transaction.commit();
    }
  }

  @Override
  public void rollback(boolean required) throws DataAccessException {
    if (!closed && required) {
      transaction.rollback();
    }
  }

  /**
   * Apply a transaction timeout.
   *
   * @param statement a current statement
   * @throws SQLException if a database access error occurs, this method is called on a closed
   *     <code>Statement</code>
   * @since 3.4.0
   */
  protected void applyTransactionTimeout(Statement statement) throws SQLException {
    final int queryTimeout = statement.getQueryTimeout();
    final Integer transactionTimeout = transaction.getTimeout();
    if (transactionTimeout == null) {
      return;
    }
    if (queryTimeout == 0 || transactionTimeout < queryTimeout) {
      statement.setQueryTimeout(transactionTimeout);
    }
  }

  @Override
  public void setExecutorWrapper(Executor wrapper) {
    this.wrapper = wrapper;
  }

  @Override
  public void setLog(final Log log) {
    this.log = log;
  }
}
