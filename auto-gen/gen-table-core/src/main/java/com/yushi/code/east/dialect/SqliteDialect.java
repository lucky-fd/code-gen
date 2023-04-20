/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.yushi.code.east.dialect;

import com.yushi.code.east.dialect.identity.IdentityColumnSupport;
import com.yushi.code.east.dialect.identity.SQLiteIdentityColumnSupport;

import java.math.BigDecimal;
import java.util.Date;

/** An SQL dialect for Sqlite. */
public class SqliteDialect extends Dialect {

  /** Constructs a MySQLDialect */
  public SqliteDialect() {
    super();
    // char type
    registerColumnType(Character.class, "TEXT");
    registerColumnType(char.class, "TEXT");
    // boolean type
    registerColumnType(Boolean.class, "INTEGER");
    registerColumnType(boolean.class, "INTEGER");
    // value type
    registerColumnType(Float.class, "REAL");
    registerColumnType(float.class, "REAL");

    registerColumnType(Double.class, "REAL");
    registerColumnType(double.class, "REAL");

    registerColumnType(BigDecimal.class, "REAL");
    // date type
    registerColumnType(Date.class, "TEXT");
    // varchar type
    registerColumnType(String.class, "TEXT");

  }

  @Override
  public String getAddColumnString() {
    return "add column";
  }

  @Override
  public boolean isSupportComment() {
    return false;
  }

  @Override
  public IdentityColumnSupport getIdentityColumnSupport() {
    return new SQLiteIdentityColumnSupport();
  }

  @Override
  public String getKeyHolderKey() {
    return "last_insert_rowid()";
  }
}
