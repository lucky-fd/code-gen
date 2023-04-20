package com.yushi.code.east.exception;


import javax.annotation.Nullable;

/** 公共数据库访问异常. */
public class DataAccessException extends WindException {
  public DataAccessException(String message) {
    super(message);
  }

  public DataAccessException(@Nullable Throwable cause) {
    super(cause);
  }

  public DataAccessException(@Nullable String message, @Nullable Throwable cause) {
    super(message, cause);
  }
}
