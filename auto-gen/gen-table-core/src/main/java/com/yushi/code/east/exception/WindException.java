package com.yushi.code.east.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局通用异常.
 *
 * @since 2020.12.22
 * @author fdong
 */
@Slf4j
public class WindException extends RuntimeException {

  public WindException(final String message) {
    super(message);
  }

  public WindException(final Throwable cause) {
    super(cause);
  }

  public WindException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
