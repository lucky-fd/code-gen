package com.yushi.code.east.exception;

/**
 * 未返回期望的结果数量.
 *
 * @since 2021.01.31
 * @author fdong
 */
public class TooManyResultException extends DataAccessException {
  private final int count;

  public TooManyResultException(final String message, final int count) {
    super("Too Many Result");
    this.count = count;
  }

  public TooManyResultException(final int count) {
    super("Too many result exception");
    this.count = count;
  }

  @Override
  public String toString() {
    return "Unexpect result,Expect one but get " + count;
  }
}
