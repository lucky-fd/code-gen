package com.yushi.code.east.condition;

import com.yushi.code.east.domain.Pageable;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 条件对象.
 *
 * @param <POJO> pojo对象
 * @param <CONDITION> 当前对象
 * @since 2020.01.06
 * @author fdong
 */
public interface Condition<POJO, CONDITION extends Condition<POJO, CONDITION>> {
  Class<POJO> getClazz();

  /**
   * 获取分页和排序参数.示例:
   *
   * <pre>
   *     <li><code>PageRequest.of(1);</code>
   *     <li><code>PageRequest.of(1, 10).desc(Foo::setId);</code>
   *     <li><code>cnds.limit(1);</code>
   *     <li><code>cnds.limit(1, 10).orderBy(Foo::setId);</code>
   *     <li><code>Pageable.unpaged();</code>
   * </pre>
   */
  @Nonnull
  Pageable getPageRequest();

  String getString();

  /**
   * 获取占位符值.<br>
   *
   * @param startIndex 填充参数的起始索引,null时从0开始
   * @return 占位符对应的值 values
   */
  List<Consumer<PreparedStatement>> getValues(final AtomicInteger startIndex);

  /** 获取所有原始值(未经过转换器转换的值). */
  List<Object> getOriginValues();

  /**
   * 是否为空,true:不包含任何条件.
   *
   * @return the boolean
   */
  boolean isEmpty();

  default CONDITION eq(@Nonnull final Field field, final Object value) {
    return eq(true, field, value);
  }

  CONDITION eq(final boolean condition, @Nonnull final Field field, final Object value);

  default CONDITION in(@Nonnull Field field, @Nonnull Collection<?> values) {
    return in(true, field, values);
  }

  CONDITION in(final boolean condition, @Nonnull Field field, @Nonnull Collection<?> values);

  default CONDITION and(final String sql) {
    return and(true, sql);
  }

  CONDITION and(final boolean condition, final String sql);

  default CONDITION or(final String sql) {
    return or(true, sql);
  }

  CONDITION or(final boolean condition, final String sql);

  /** 属性匹配模式 */
  enum MatchPattern {
    /** = */
    EQUAL("="),
    /** &lt;&gt; */
    NOT_EQUAL("<>"),
    /** &gt; */
    GREATER(">"),
    /** &gt;= */
    GE(">="),
    /** &lt; */
    LESS("<"),
    /** &lt;= */
    LE("<="),
    LIKE_PLAIN(" like %s "),
    /** LIKE %criteria% */
    LIKE(" like '%%%s%%'"),
    /** LIKE %criteria */
    LIKE_LEFT(" like '%%%s'"),
    /** LIKE criteria% */
    LIKE_RIGHT(" like '%s%%'"),
    /** NOT LIKE %criteria% */
    NOT_LIKE_PLAIN(" not like %s "),
    NOT_LIKE(" not like '%%%s%%'"),
    /** BETWEEN start AND end */
    BETWEEN(" between %s and %s"),
    /** NOT BETWEEN start AND end */
    NOT_BETWEEN(" not between %s and %s"),
    /** IS NULL */
    IS_NULL(" is null"),
    /** IS NOT NULL */
    IS_NOT_NULL(" is not null"),
    /** EXISTS */
    EXISTS(" exists(%s)"),
    /** NOT EXISTS */
    NOT_EXISTS(" not exists(%s)"),
    /** IN () */
    IN(" in (%s)"),
    /** NOT IN () */
    NOT_IN(" not in (%s)"),

    /** ANY() */
    ANY(" any(%s)"),

    /** &gt;ANY() */
    GREATER_ANY(" >any(%s)"),
    /** &gt;=ANY() */
    GE_ANY(" >=any(%s)"),
    /** &lt;ANY() */
    LESS_ANY(" <any(%s)"),
    /** &lt;=ANY() */
    LE_ANY(" <=any(%s)");

    final String operator;

    public String operator() {
      return operator;
    }

    MatchPattern(final String operator) {
      this.operator = operator;
    }
  }

  enum SqlOperator {
    /** 点. */
    DOT("."),
    /** 逗号. */
    COMMA(","),
    /** 问号. */
    QUESTION_MARK("?"),
    /** 百分号. */
    PERCENT("%"),
    /** 星号. */
    WILDCARD("*"),
    /** 等于. */
    EQUAL("="),
    EQUAL_FORMAT(" %s=%s "),

    /** 引号. */
    QUOTE("'"),
    QUOTE_FORMAT("'%s'"),

    /** 圆括号(小括号). */
    LEFT_PARENTHESIS("("),
    RIGHT_PARENTHESIS(")"),
    PARENTHESIS_FORMAT("(%s)"),

    /** 花括号(大括号). */
    LEFT_BRACE("{"),
    RIGHT_BRACE("}"),
    BRACE_FORMAT("{%s}"),

    /** 方括号(中括号) */
    LEFT_SQUARE_BRACKETS("["),
    RIGHT_SQUARE_BRACKETS("["),
    SQUARE_BRACKETS_FORMAT("[%s]"),

    INSERT_INTO("insert into "),

    VALUES(" values"),

    WHERE(" where "),

    AS(" as "),

    AND(" and "),

    ORDER_BY(" order by "),

    GROUP_BY(" group by "),

    OR(" or "),
    /** 任意 */
    ANY(" any "),
    /** 包含 */
    CONTAIN(" @> "),
    /** 被包含 */
    CONTAINED(" <@ "),
    /** 重叠 */
    OVERLAP(" && "),
    /** 连接 */
    CONCATENATES(" || "),
    ;

    final String operator;

    SqlOperator(final String operator) {
      this.operator = operator;
    }

    public String operator() {
      return this.operator;
    }

    public String format(final Object... string) {
      return String.format(this.operator, string);
    }

    @Override
    public String toString() {
      return this.operator;
    }
  }
}
