package com.yushi.code.tableCreator.jdbc.dynamicdatasource;

import com.yushi.code.tableCreator.exception.WindException;

/**
 * @author fdong
 * @since 2022.03.20
 */
public class DataSourceNotFoundException extends WindException {
  public DataSourceNotFoundException(final String message) {
    super(message);
  }

  public DataSourceNotFoundException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public DataSourceNotFoundException(final Throwable cause) {
    super(cause);
  }
}
