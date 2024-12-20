/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.yushi.code.east.dialect.mysql;

import java.util.Date;

/** @author Gail Badner */
public class MySQL57Dialect extends MySQL55Dialect {
  public MySQL57Dialect() {
    super();

    // For details about MySQL 5.7 support for fractional seconds
    // precision (fsp): http://dev.mysql.com/doc/refman/5.7/en/fractional-seconds.html
    // Regarding datetime(fsp), "The fsp value, if given, must be
    // in the range 0 to 6. A value of 0 signifies that there is
    // no fractional part. If omitted, the default precision is 0.
    // (This differs from the standard SQL default of 6, for
    // compatibility with previous MySQL versions.)".

    // The following is defined because Hibernate currently expects
    // the SQL 1992 default of 6 (which is inconsistent with the MySQL
    // default).
    registerColumnType(Date.class, "datetime(6)");

    // MySQL 5.7 brings JSON native support with a dedicated datatype.
    // For more details about MySql new JSON datatype support, see:
    // https://dev.mysql.com/doc/refman/5.7/en/json.html
    // TODO WARN MYSQL JSON 对应Java类型
    // registerColumnType(Types.JAVA_OBJECT, "json");
  }

  /**
   * @see <a href="https://dev.mysql.com/worklog/task/?id=7019">MySQL 5.7 work log</a>
   * @return supports IN clause row value expressions
   */
  public boolean supportsRowValueConstructorSyntaxInInList() {
    return true;
  }
}
