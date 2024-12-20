package com.yushi.code.east.domain;

import com.yushi.code.east.function.GetterFunction;
import lombok.NonNull;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * 排序规则.示例: <code>PageRequest.of(1, 10).asc(Foo::getUpdateTime)</code>
 *
 * @author fdong
 * @since 2020 /1/5
 */
public class PageRequest implements Pageable {
  /** 排序规则. */
  private Sort sort = Sort.unsorted();
  /** 当前页,从1开始. */
  private int page = 1;
  /** 每页大小. */
  private int size;

  private PageRequest() {}

  @NonNull
  public static <T> PageRequest of(final int size) {
    return of(1, size, Collections.emptyList());
  }

  @NonNull
  public static <T> PageRequest of(final int page, final int size) {
    return of(page, size, Collections.emptyList());
  }

  @NonNull
  public static <T> PageRequest of(
      final int page, final int size, @Nonnull final List<Sort.Order> orders) {
    return of(page, size, Sort.by(orders));
  }

  @NonNull
  public static <T> PageRequest of(final int page, final int size, Sort sort) {
    if (page < 1) {
      throw new IllegalArgumentException("Page index must not be less than 1!");
    }
    if (size < 1) {
      throw new IllegalArgumentException("Size must not be less than 1!");
    }
    final PageRequest pageRequest = new PageRequest();
    pageRequest.page = page;
    pageRequest.size = size;
    pageRequest.sort = sort;
    return pageRequest;
  }

  public <T> PageRequest asc(final GetterFunction<T, ?> getter) {
    return asc(getter.getColumn());
  }

  /**
   * {@link PageRequest#asc(GetterFunction)}
   *
   * @param column the column
   * @return the sort column
   */
  public PageRequest asc(final String column) {
    sort.and(Sort.by(Sort.Direction.ASC, column));
    return this;
  }

  public <T> PageRequest desc(final GetterFunction<T, ?> getter) {
    return desc(getter.getColumn());
  }

  /**
   * {@link PageRequest#desc(GetterFunction)}
   *
   * @param column the column
   * @return the sort column
   */
  public PageRequest desc(final String column) {
    sort.and(Sort.by(Sort.Direction.DESC, column));
    return this;
  }

  @Override
  public int getPageNumber() {
    return page;
  }

  @Override
  public int getPageSize() {
    return size;
  }

  @Override
  public long getOffset() {
    return (long) (page - 1) * (long) size;
  }

  @Override
  public Pageable next() {
    return PageRequest.of(getPageNumber() + 1, getPageSize(), getSort());
  }

  @Override
  public Pageable previous() {
    return getPageNumber() == 1
        ? this
        : PageRequest.of(getPageNumber() - 1, getPageSize(), getSort());
  }

  @Override
  public Sort getSort() {
    return sort;
  }
}
