/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.yushi.code.east.dialect.identity;

import java.lang.reflect.Type;

/** @author Andrea Boriero */
public class MySQLIdentityColumnSupport extends IdentityColumnSupportImpl {

  @Override
  public String getIdentityColumnString(Type type) {
    // starts with 1, implicitly
    return "not null auto_increment";
  }
}
