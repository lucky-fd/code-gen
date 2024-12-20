/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.yushi.code.east.dialect.postgresql;

import com.yushi.code.east.dialect.Dialect;
import com.yushi.code.east.dialect.identity.IdentityColumnSupport;
import com.yushi.code.east.dialect.identity.PostgreSQL81IdentityColumnSupport;
import com.yushi.code.east.type.JavaType;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.BitSet;
import java.util.Date;

/**
 * An SQL dialect for Postgres
 *
 * <p>For discussion of BLOB support in Postgres, as of 8.4, have a peek at <a
 * href="http://jdbc.postgresql.org/documentation/84/binary-data.html">http://jdbc.postgresql.org/documentation/84/binary-data.html</a>.
 * For the effects in regards to Hibernate see <a
 * href="http://in.relation.to/15492.lace">http://in.relation.to/15492.lace</a>
 *
 * @author Gavin King
 */
@Slf4j
public class PostgreSQL81Dialect extends Dialect {
  public PostgreSQL81Dialect() {
    super();
    // boolean type
    registerColumnType(boolean.class, "bool");
    registerColumnType(Boolean.class, "bool");

    registerColumnType(String.class, "text");
    /// registerColumnType(String.class, 255, "text($l)");
    // registerColumnType(String.class, 65535, "text($l)");
    // date type
    registerColumnType(Date.class, "timestamp(0)");
    registerColumnType(LocalDate.class, "date");
    registerColumnType(LocalTime.class, "time");
    registerColumnType(LocalDateTime.class, "timestamp(0)");
    // array
    registerColumnType(short[].class, "int[]");
    registerColumnType(Short[].class, "int[]");
    registerColumnType(JavaType.LIST_SHORT, "int[]");
    registerColumnType(JavaType.SET_SHORT, "int[]");

    registerColumnType(int[].class, "int[]");
    registerColumnType(Integer[].class, "int[]");
    registerColumnType(JavaType.LIST_INTEGER, "int[]");
    registerColumnType(JavaType.SET_INTEGER, "int[]");

    registerColumnType(long[].class, "bigint[]");
    registerColumnType(Long[].class, "bigint[]");
    registerColumnType(JavaType.LIST_LONG, "bigint[]");
    registerColumnType(JavaType.SET_LONG, "bigint[]");

    registerColumnType(String[].class, "text[]");
    registerColumnType(JavaType.LIST_STRING, "text[]");
    registerColumnType(JavaType.SET_STRING, "text[]");
  }

  @Override
  public String getAddColumnString() {
    return "add column";
  }

  @Override
  public void addSupportedJavaTypes() {
    super.addSupportedJavaTypes();

    addSupportedJavaType(char[].class);
    addSupportedJavaType(Character[].class);

    addSupportedJavaType(short[].class);
    addSupportedJavaType(Short[].class);
    addSupportedJavaType(int[].class);
    addSupportedJavaType(Integer[].class);
    addSupportedJavaType(long[].class);
    addSupportedJavaType(Long[].class);

    addSupportedJavaType(String[].class);

    addSupportedJavaType(JavaType.LIST_SHORT);
    addSupportedJavaType(JavaType.LIST_INTEGER);
    addSupportedJavaType(JavaType.LIST_LONG);
    addSupportedJavaType(JavaType.LIST_FLOAT);
    addSupportedJavaType(JavaType.LIST_DOUBLE);
    addSupportedJavaType(JavaType.LIST_BIGDECIMAL);
    addSupportedJavaType(JavaType.LIST_STRING);

    addSupportedJavaType(JavaType.SET_SHORT);
    addSupportedJavaType(JavaType.SET_INTEGER);
    addSupportedJavaType(JavaType.SET_LONG);
    addSupportedJavaType(JavaType.SET_FLOAT);
    addSupportedJavaType(JavaType.SET_DOUBLE);
    addSupportedJavaType(JavaType.SET_BIGDECIMAL);
    addSupportedJavaType(JavaType.SET_STRING);

    addSupportedJavaType(BitSet.class);
  }

  @Override
  public String getCommonOnTableString(
      final String category, final String schema, final String table, final String comment) {
    return String.format("comment on table %s is '%s'", table, comment);
  }

  @Override
  public String getCommentOnColumnString(
      final String table, final String column, final String comment) {
    return String.format("comment on column %s.%s is '%s'", table, column, comment);
  }

  @Override
  public boolean isSupportCommentOn() {
    return true;
  }

  @Override
  public IdentityColumnSupport getIdentityColumnSupport() {
    return new PostgreSQL81IdentityColumnSupport();
  }

  @Override
  public String getKeyHolderKey() {
    return null;
  }
}
