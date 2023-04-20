package com.yushi.code.east.condition;

import com.yushi.code.east.domain.PageRequest;
import com.yushi.code.east.domain.Pageable;
import com.yushi.code.east.domain.Sort;
import com.yushi.code.east.function.GetterFunction;
import com.yushi.code.east.helper.SqlHelper;
import com.yushi.code.east.util.EntityUtils;
import com.yushi.code.east.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.yushi.code.east.condition.Condition.SqlOperator.*;

/**
 * 条件构造.
 *
 * @param <POJO> the type parameter
 * @param <CONDITION> 当前对象
 * @since 2020.09.16
 * @author fdong
 */
@Slf4j
public abstract class AbstractCnd<POJO, CONDITION extends AbstractCnd<POJO, CONDITION>>
    implements Condition<POJO, CONDITION> {

  /** 从1开始 */
  protected int page;

  protected int size;
  private final List<Sort.Order> orders = new LinkedList<>();

  private Class<POJO> clazz;

  /** where后的字符串,参数占位符为 ?. */
  protected final List<String> conditionSql = new LinkedList<>();

  protected final List<String> groupBySql = new LinkedList<>();

  /** 占位符对应的值. */
  /** 是否拼接逻辑删除条件. */
  private boolean hasAppendNotDeleteCondition = false;

  protected AbstractCnd() {}

  public AbstractCnd(@Nonnull final Class<POJO> clazz) {
    this.clazz = clazz;
  }

  @Override
  public Class<POJO> getClazz() {
    return clazz;
  }

  /** 直接拼接sql,括号需要手动加.如: {@code (id=1 and name like 'ramer%')} */
  @Override
  public CONDITION and(final boolean condition, final String sql) {
    if (condition && StringUtils.nonEmpty(sql)) {
      conditionSql.add((conditionSql.size() > 0 ? AND.operator : "").concat(sql));
    }
    //noinspection unchecked
    return (CONDITION) this;
  }

  /** 直接拼接sql,括号需要手动加.如: {@code (id=1 and name like 'ramer%')} */
  @Override
  public CONDITION or(final boolean condition, final String sql) {
    if (condition && StringUtils.nonEmpty(sql)) {
      conditionSql.add((conditionSql.size() > 0 ? OR.operator : "").concat(sql));
    }
    //noinspection unchecked
    return (CONDITION) this;
  }

  /** 从第一页开始,限制获取记录数. */
  public CONDITION limit(final int size) {
    return limit(1, size);
  }

  /** 分页参数.page 从1开始 */
  public CONDITION limit(final int page, final int size) {
    if (page < 1 || size < 1) {
      throw new IllegalArgumentException("page,size不能小于1");
    }
    this.page = page;
    this.size = size;
    //noinspection unchecked
    return (CONDITION) this;
  }

  /** 默认倒序 desc. */
  public CONDITION orderBy(@Nonnull final GetterFunction<POJO, ?> getter) {
    orders.add(Sort.Order.desc(getter.getColumn()));
    //noinspection unchecked
    return (CONDITION) this;
  }

  public CONDITION orderBy(
      @Nonnull final GetterFunction<POJO, ?> getter, @Nonnull final Sort.Direction direction) {
    orders.add(
        direction.isAscending() ? Sort.Order.asc(getter.getColumn()) : Sort.Order.desc(getter.getColumn()));
    //noinspection unchecked
    return (CONDITION) this;
  }

  public CONDITION asc(@Nonnull final GetterFunction<POJO, ?> getter) {
    return orderBy(getter, Sort.Direction.ASC);
  }

  public CONDITION desc(@Nonnull final GetterFunction<POJO, ?> getter) {
    return orderBy(getter, Sort.Direction.DESC);
  }

  @Nonnull
  @Override
  public Pageable getPageRequest() {
    return page > 0 && size > 0
        ? PageRequest.of(page, size, orders)
        : !orders.isEmpty() ? Pageable.unpaged(orders) : Pageable.unpaged();
  }

  @Override
  public String getString() {
    if (groupBySql.isEmpty()) {
      return String.join("", conditionSql);
    }
    return String.join("", conditionSql)
        .concat(GROUP_BY.operator)
        .concat(String.join(",", groupBySql));
  }

  @Override
  public String toString() {
    return getString();
  }

  @Override
  public List<Consumer<PreparedStatement>> getValues(final AtomicInteger startIndex) {
    return new ArrayList<>();
  }

  @Override
  public List<Object> getOriginValues() {
    return new ArrayList<>();
  }


  @Override
  public final CONDITION eq(
      final boolean condition, @Nonnull final Field field, final Object value) {
    if (condition) {
      conditionSql.add(
          (conditionSql.size() > 0 ? AND.operator : "")
              .concat(EntityUtils.fieldToColumn(field))
              .concat(MatchPattern.EQUAL.operator)
              .concat(SqlHelper.toPreFormatSqlVal(value)));
    }
    //noinspection unchecked
    return (CONDITION) this;
  }

  @Override
  public final CONDITION in(
      final boolean condition, @Nonnull final Field field, @Nonnull final Collection<?> values) {
    if (condition) {
      conditionSql.add(
          (conditionSql.size() > 0 ? AND.operator : "")
              .concat(EntityUtils.fieldToColumn(field))
              .concat(
                  String.format(
                      MatchPattern.IN.operator,
                      values.stream()
                          .map(SqlHelper::toPreFormatSqlVal)
                          .collect(Collectors.joining(COMMA.operator)))));
    }
    //noinspection unchecked
    return (CONDITION) this;
  }
}
