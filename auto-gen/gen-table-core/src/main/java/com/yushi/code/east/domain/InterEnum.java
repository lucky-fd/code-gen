package com.yushi.code.east.domain;

import com.yushi.code.east.exception.InvalidEnumException;
import com.yushi.code.east.exception.WindException;
import com.yushi.code.east.util.InterEnumUtils;
import com.yushi.code.east.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.function.Supplier;

/**
 * The interface inter enum.
 *
 * @param <V> the type parameter
 * @author fdong
 * @since 2020 /3/28
 */
public interface InterEnum<V> extends Serializable {
  /** The constant log. */
  Logger log = LoggerFactory.getLogger(InterEnum.class);

  /**
   * Value integer.
   *
   * @return the integer
   */
  @Nonnull
  V value();

  /**
   * Desc string.
   *
   * @return the string
   */
  @Nonnull
  String desc();

  /**
   * 通过value获取枚举,可能为null.
   *
   * @param <V> the type parameter
   * @param <E> the type parameter
   * @param value 枚举值
   * @param clazz 实现{@link InterEnum}的枚举类
   * @return 枚举实例 e
   */
  static <V, E extends InterEnum<V>> E ofNullable(final V value, Class<E> clazz) {
    return InterEnumUtils.of(value, clazz);
  }

  /**
   * 通过value获取枚举,当返回null时,抛出包含<code>message</code>的异常.
   *
   * @param <V> the type parameter
   * @param <E> the type parameter
   * @param value 枚举值
   * @param clazz the clazz
   * @return 枚举实例 e
   * @throws WindException the common exception
   */
  static <V, E extends InterEnum<V>> E of(final V value, Class<E> clazz, Supplier<String> message)
      throws WindException {
    E e = InterEnumUtils.of(value, clazz);
    if (e == null) {
      throw new InvalidEnumException(
          String.format(
              "枚举值无效:[%s]",
              message != null && message.get() != null
                  ? message.get()
                  : StringUtils.firstLowercase(clazz.getSimpleName())));
    }
    return e;
  }
}
