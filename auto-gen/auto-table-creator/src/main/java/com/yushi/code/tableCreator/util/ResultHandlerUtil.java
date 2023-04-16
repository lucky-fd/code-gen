package com.yushi.code.tableCreator.util;

import com.yushi.code.tableCreator.handler.BeanResultHandler;
import com.yushi.code.tableCreator.handler.PrimitiveResultHandler;
import com.yushi.code.tableCreator.handler.ResultHandler;

import javax.annotation.Nullable;

/**
 * @author fdong
 * @since 30/08/2021
 */
public final class ResultHandlerUtil {
  public static <E, R> ResultHandler<R> getResultHandler(final Class<R> clazz) {
    return getResultHandler(clazz, null);
  }

  public static <E, R> ResultHandler<R> getResultHandler(
      final Class<R> clazz, @Nullable final ResultHandler<R> defaultResultHandler) {
    if (defaultResultHandler != null) {
      return defaultResultHandler;
    }
    return clazz.getClassLoader() == null || clazz.isArray()
        ? new PrimitiveResultHandler<>(clazz)
        : new BeanResultHandler<>(clazz);
  }
}
