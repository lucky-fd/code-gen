package com.yushi.code.east.condition.function;

/**
 * sql function.
 *
 * @author fdong
 * @since 2020 /4/29
 */
public interface SqlFunction {

  /**
   * 获取sql.
   *
   * @param str 通常情况下该值为空.具体由函数决定,可能为列名
   * @return sql
   */
  String string(final String... str);
}
