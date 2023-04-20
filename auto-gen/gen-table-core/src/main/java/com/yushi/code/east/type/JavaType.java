package com.yushi.code.east.type;

import com.yushi.code.east.util.TypeUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Java类型.用于与sql类型对应.
 *
 * @author fdong
 * @since 2020.08.21
 */
public interface JavaType {
  Type LIST_SHORT = TypeUtils.getType(List.class, Short.class);
  Type LIST_INTEGER = TypeUtils.getType(List.class, Integer.class);
  Type LIST_LONG = TypeUtils.getType(List.class, Long.class);
  Type LIST_FLOAT = TypeUtils.getType(List.class, Float.class);
  Type LIST_DOUBLE = TypeUtils.getType(List.class, Double.class);
  Type LIST_BIGDECIMAL = TypeUtils.getType(List.class, BigDecimal.class);
  Type LIST_STRING = TypeUtils.getType(List.class, String.class);

  Type SET_SHORT = TypeUtils.getType(Set.class, Short.class);
  Type SET_INTEGER = TypeUtils.getType(Set.class, Integer.class);
  Type SET_LONG = TypeUtils.getType(Set.class, Long.class);
  Type SET_FLOAT = TypeUtils.getType(Set.class, Float.class);
  Type SET_DOUBLE = TypeUtils.getType(Set.class, Double.class);
  Type SET_BIGDECIMAL = TypeUtils.getType(Set.class, BigDecimal.class);
  Type SET_STRING = TypeUtils.getType(Set.class, String.class);

  /**
   * 可映射的数据库类型.
   *
   * @return the string [ ]
   */
  String[] getSqlTypes();
}
