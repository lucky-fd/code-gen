package com.yushi.code.east.executor;

import com.yushi.code.east.config.EastConfiguration;
import com.yushi.code.east.domain.Page;
import com.yushi.code.east.domain.Pageable;
import com.yushi.code.east.exception.DataAccessException;
import com.yushi.code.east.exception.TooManyResultException;
import com.yushi.code.east.executor.logging.ConnectionLogger;
import com.yushi.code.east.executor.logging.Log;
import com.yushi.code.east.executor.logging.SimpleLog;
import com.yushi.code.east.handler.MapResultHandler;
import com.yushi.code.east.handler.PrimitiveResultHandler;
import com.yushi.code.east.handler.ResultHandler;
import com.yushi.code.east.jdbc.managed.Transaction;
import com.yushi.code.east.util.DataSourceUtils;
import com.yushi.code.east.util.JdbcUtils;
import com.yushi.code.east.util.ResultHandlerUtil;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The simple jdbc executor.
 *
 * @author fdong
 * @since 2022.03.19
 */
public class SimpleJdbcExecutor extends BaseExecutor implements Executor {

    public SimpleJdbcExecutor(final EastConfiguration eastConfiguration, final Transaction transaction) {
        this(eastConfiguration, transaction, new SimpleLog(SimpleJdbcExecutor.class));
    }

    public SimpleJdbcExecutor(
            final EastConfiguration eastConfiguration, final Transaction transaction, final Log log) {
        super(eastConfiguration, transaction, log);
    }

    @Override
    public <T, R> R fetchOne(@Nonnull final SqlParam<T> sqlParam) throws DataAccessException {
        return fetchOne(sqlParam, null);
    }

    @Override
    public <T, R> R fetchOne(@Nonnull final SqlParam<T> sqlParam, ResultHandler<R> resultHandler)
            throws DataAccessException {

        @SuppressWarnings("unchecked") final Class<R> clazz = (Class<R>) sqlParam.getClazz();
        final List<R> result =
                query(
                        sqlParam,
                        ps -> sqlParam.condition.getValues(sqlParam.startIndex).forEach(o -> o.accept(ps)),
                        ResultHandlerUtil.getResultHandler(clazz, resultHandler));
        if (result.isEmpty()) {
            return null;
        }
        if (result.size() > 1) {
            throw new TooManyResultException("fetch one", result.size());
        }
        return result.get(0);
    }

    @Override
    public <T, R> List<R> fetchAll(@Nonnull final SqlParam<T> sqlParam, final Class<R> clazz)
            throws DataAccessException {
        return query(
                sqlParam,
                ps -> sqlParam.condition.getValues(sqlParam.startIndex).forEach(o -> o.accept(ps)),
                ResultHandlerUtil.getResultHandler(clazz));
    }

    @Override
    public <T, R> Page<R> fetchPage(
            @Nonnull final SqlParam<T> sqlParam, final Pageable pageable, final long total)
            throws DataAccessException {
        @SuppressWarnings("unchecked")
        Class<R> clazz = (Class<R>) sqlParam.clazz;
        final List<R> content =
                total < 1
                        ? Collections.emptyList()
                        : query(
                        sqlParam,
                        ps -> sqlParam.condition.getValues(sqlParam.startIndex).forEach(o -> o.accept(ps)),
                        ResultHandlerUtil.getResultHandler(clazz));
        return Page.of(content, pageable == null ? Pageable.unpaged() : pageable, total);
    }

    @Override
    public <T> long fetchCount(@Nonnull final SqlParam<T> sqlParam) {
        List<Long> result =
                query(
                        sqlParam,
                        ps -> sqlParam.condition.getValues(sqlParam.startIndex).forEach(o -> o.accept(ps)),
                        new PrimitiveResultHandler<>(Long.class));
        if (result.isEmpty()) {
            return 0L;
        }
        return result.get(0);
    }

    @Override
    public <T, R> R queryForObject(@Nonnull final SqlParam<T> sqlParam, final Object[] args)
            throws DataAccessException {
        @SuppressWarnings("unchecked") final Class<R> clazz = (Class<R>) sqlParam.getClazz();
        final List<R> result =
                query(
                        sqlParam,
                        ps -> {
                            AtomicInteger startIndex = sqlParam.startIndex;
                            for (Object arg : args) {
                                JdbcUtils.setObject(ps, startIndex.getAndIncrement(), arg);
                            }
                        },
                        ResultHandlerUtil.getResultHandler(clazz));
        if (result.isEmpty()) {
            return null;
        }
        if (result.size() > 1) {
            throw new TooManyResultException("query for object", result.size());
        }
        return result.get(0);
    }

    @Override
    public Map<String, Object> queryForMap(@Nonnull final SqlParam<?> sqlParam, final Object... args)
            throws DataAccessException {
        List<Map<String, Object>> maps = queryForList(sqlParam, args);
        return maps.isEmpty() ? Collections.emptyMap() : maps.get(0);
    }

    @Override
    public List<Map<String, Object>> queryForList(
            @Nonnull final SqlParam<?> sqlParam, final Object... args) throws DataAccessException {
        return query(
                sqlParam,
                ps -> {
                    AtomicInteger startIndex = sqlParam.startIndex;
                    for (Object arg : args) {
                        JdbcUtils.setObject(ps, startIndex.getAndIncrement(), arg);
                    }
                },
                new MapResultHandler());
    }

    @Override
    public <T> List<T> query(
            @Nonnull final SqlParam<?> sqlParam,
            final PreparedStatementSetter pss,
            final ResultHandler<T> resultHandler)
            throws DataAccessException {
        Connection connection = getConnection();
        PreparedStatement ps = DataSourceUtils.preparedStatement(connection, sqlParam.sql);
        if (pss != null) {
            pss.setValues(ps);
        }
        ResultSet resultSet;
        try {
            resultSet = ps.executeQuery();
        } catch (Exception e) {
            DataSourceUtils.close(ps);
            DataSourceUtils.releaseConnection(connection);
            throw new DataAccessException("Fail to execute query:" + sqlParam.sql, e);
        }
        List<T> ts = new ArrayList<>();
        try {
            while (resultSet.next()) {
                ts.add(resultHandler.handle(resultSet));
            }
        } catch (Exception e) {
            throw new DataAccessException("Fail to handle resultSet:" + sqlParam.sql, e);
        } finally {
            DataSourceUtils.close(resultSet);
            DataSourceUtils.close(ps);
            transaction.releaseConnection();
        }
        return ts;
    }

    @Override
    public int update(final PreparedStatementCreator psc, final KeyHolder generatedKeyHolder)
            throws DataAccessException {
        Connection connection = getConnection();
        PreparedStatement ps = psc.createPreparedStatement(connection);
        try {
            List<Map<String, Object>> generatedKeys = generatedKeyHolder.getKeyList();
            generatedKeys.clear();

            int rows = ps.executeUpdate();
            MapResultHandler resultHandler = new MapResultHandler();
            ResultSet resultSet = ps.getGeneratedKeys();
            while (resultSet.next()) {
                generatedKeys.add(resultHandler.handle(resultSet));
            }
            return rows;
        } catch (Exception e) {
            throw new DataAccessException("Fail to exexute update", e);
        } finally {
            DataSourceUtils.close(ps);
            transaction.releaseConnection();
        }
    }

    @Override
    public int[] batchUpdate(String sql, final BatchPreparedStatementSetter pss)
            throws DataAccessException {
        Connection connection = getConnection();
        PreparedStatement ps = DataSourceUtils.preparedStatement(connection, sql);
        int batchSize = pss.getBatchSize();
        try {
            if (JdbcUtils.supportsBatchUpdates(connection)) {
                for (int i = 0; i < batchSize; i++) {
                    pss.setValues(ps, i);
                    ps.addBatch();
                }
                return ps.executeBatch();
            } else {
                int[] rowsAffectedArray = new int[batchSize];
                for (int i = 0; i < batchSize; i++) {
                    pss.setValues(ps, i);
                    rowsAffectedArray[i] = ps.executeUpdate();
                }
                return rowsAffectedArray;
            }
        } catch (Exception e) {
            throw new DataAccessException("Fail to execute batch update", e);
        } finally {
            DataSourceUtils.close(ps);
            transaction.releaseConnection();
        }
    }

    @Override
    public int[] batchUpdate(
            final PreparedStatementCreator psc,
            final BatchPreparedStatementSetter pss,
            final KeyHolder generatedKeyHolder)
            throws DataAccessException {
        Connection connection = getConnection();
        PreparedStatement ps = psc.createPreparedStatement(connection);
        ResultSet resultSet = null;
        int batchSize = pss.getBatchSize();
        try {
            if (JdbcUtils.supportsBatchUpdates(connection)) {
                for (int i = 0; i < batchSize; i++) {
                    pss.setValues(ps, i);
                    ps.addBatch();
                }
                final int[] rows = ps.executeBatch();
                List<Map<String, Object>> generatedKeys = generatedKeyHolder.getKeyList();
                generatedKeys.clear();
                MapResultHandler resultHandler = new MapResultHandler();
                resultSet = ps.getGeneratedKeys();
                while (resultSet.next()) {
                    generatedKeys.add(resultHandler.handle(resultSet));
                }
                return rows;
            } else {
                int[] rowsAffectedArray = new int[batchSize];
                for (int i = 0; i < batchSize; i++) {
                    pss.setValues(ps, i);
                    rowsAffectedArray[i] = ps.executeUpdate();
                }
                return rowsAffectedArray;
            }
        } catch (Exception e) {
            throw new DataAccessException("Fail to execute batch update", e);
        } finally {
            DataSourceUtils.close(resultSet);
            DataSourceUtils.close(ps);
            transaction.releaseConnection();
        }
    }

    @Override
    public int[] batchUpdate(final String[] sqls) {
        Connection connection = getConnection();
        Statement statement = DataSourceUtils.statement(connection);
        try {
            if (JdbcUtils.supportsBatchUpdates(connection)) {
                try {
                    for (final String sql : sqls) {
                        statement.addBatch(sql);
                    }
                    return statement.executeBatch();
                } catch (SQLFeatureNotSupportedException e) {
                    log.warn("batchUpdate:SQLFeatureNotSupportedException:" + e.getMessage());
                }
            }

            int[] rowsAffectedArray = new int[sqls.length];
            for (int i = 0; i < sqls.length; i++) {
                final String sql = sqls[i];
                rowsAffectedArray[i] = statement.executeUpdate(sql);
            }
            return rowsAffectedArray;
        } catch (Exception e) {
            throw new DataAccessException("Fail to execute batch update" + Arrays.toString(sqls), e);
        } finally {
            DataSourceUtils.close(statement);
            transaction.releaseConnection();
        }
    }

    @Override
    public int update(String sql, @Nonnull PreparedStatementSetter pss) throws DataAccessException {
        Connection connection = getConnection();
        PreparedStatement ps = DataSourceUtils.preparedStatement(connection, sql);
        try {
            pss.setValues(ps);
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("Fail to exexute update", e);
        } finally {
            DataSourceUtils.close(ps);
            transaction.releaseConnection();
        }
    }

    private Connection getConnection() {
        final Connection connection = transaction.getConnection();
        if (log.isDebugEnabled()) {
            return ConnectionLogger.newInstance(connection, log, 1);
        } else {
            return connection;
        }
    }
}
