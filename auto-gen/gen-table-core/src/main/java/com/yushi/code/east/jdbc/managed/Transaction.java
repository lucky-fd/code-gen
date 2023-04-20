package com.yushi.code.east.jdbc.managed;

import com.yushi.code.east.exception.DataAccessException;

import java.sql.Connection;

public interface Transaction {

  Connection getConnection() throws DataAccessException;

  void releaseConnection();

  void commit() throws DataAccessException;

  void rollback() throws DataAccessException;

  void close() throws DataAccessException;

  Integer getTimeout() throws DataAccessException;
}
