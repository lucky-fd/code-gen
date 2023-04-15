package com.yushi.code.tableCreator.config;

import com.yushi.code.tableCreator.jdbc.transaction.TransactionFactory;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

/** jdbc环境. */
public final class JdbcEnvironment {
  @Getter private final TransactionFactory transactionFactory;
  @Getter private final DataSource dataSource;
  private DbMetaData dbMetaData;

  public JdbcEnvironment(
      @Nonnull final TransactionFactory transactionFactory, @Nonnull final DataSource dataSource) {
    this.transactionFactory = transactionFactory;
    this.dataSource = dataSource;
  }
}
