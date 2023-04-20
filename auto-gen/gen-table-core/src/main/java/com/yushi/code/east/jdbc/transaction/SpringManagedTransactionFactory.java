package com.yushi.code.east.jdbc.transaction;

import com.yushi.code.east.jdbc.managed.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * Creates a {@code SpringManagedTransaction}.
 *
 * @author Hunter Presnall
 */
public class SpringManagedTransactionFactory implements TransactionFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction newTransaction(
            DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new SpringManagedTransaction(dataSource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction newTransaction(Connection conn) {
        throw new UnsupportedOperationException("New Spring transactions require a DataSource");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProperties(Properties props) {
        // not needed in this version
    }
}
