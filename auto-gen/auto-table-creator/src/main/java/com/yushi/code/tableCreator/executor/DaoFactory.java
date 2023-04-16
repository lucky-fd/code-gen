package com.yushi.code.tableCreator.executor;

import io.github.ramerf.wind.core.config.Configuration;
import io.github.ramerf.wind.core.jdbc.session.TransactionIsolationLevel;

import java.sql.Connection;

/**
 * @author fdong
 * @since 2022.03.12
 */
public interface DaoFactory {
  Dao getDao();

  Dao getDao(boolean autoCommit);

  Dao getDao(TransactionIsolationLevel level);

  Dao getDao(TransactionIsolationLevel level, boolean autoCommit);

  Dao getDao(Connection connection);

  Configuration getConfiguration();
}
