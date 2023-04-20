package com.yushi.code.east.executor;


import com.yushi.code.east.config.EastConfiguration;
import com.yushi.code.east.jdbc.transaction.TransactionIsolationLevel;

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

  EastConfiguration getConfiguration();
}
