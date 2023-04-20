package com.yushi.code.east.jdbc.transaction;

import com.yushi.code.east.jdbc.managed.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;

public class JdbcTransactionFactory implements TransactionFactory {
  public JdbcTransactionFactory() {}

  @Override
  public Transaction newTransaction(Connection connection) {
    return new JdbcTransaction(connection);
  }

  @Override
  public Transaction newTransaction(
      DataSource dataSource,
      TransactionIsolationLevel transactionIsolationLevel,
      boolean autoCommit) {
    return new JdbcTransaction(dataSource, transactionIsolationLevel, autoCommit);
  }
}
