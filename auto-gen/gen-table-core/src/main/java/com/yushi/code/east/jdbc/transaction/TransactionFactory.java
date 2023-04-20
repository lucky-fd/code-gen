package com.yushi.code.east.jdbc.transaction;

import com.yushi.code.east.jdbc.managed.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

public interface TransactionFactory {
  default void setProperties(Properties props) {}

  Transaction newTransaction(Connection connection);

  Transaction newTransaction(
      DataSource dataSource,
      TransactionIsolationLevel transactionIsolationLevel,
      boolean autoCommit);
}
