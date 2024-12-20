package com.yushi.code.east.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * 反射调用异常.
 *
 * @since 2022.02.09
 * @author fdong
 */
@Slf4j
public class ReflectiveInvokeException extends WindException {

  public ReflectiveInvokeException(final String message) {
    super(message);
  }

  public ReflectiveInvokeException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ReflectiveInvokeException(final Throwable cause) {
    super(cause);
  }
}
