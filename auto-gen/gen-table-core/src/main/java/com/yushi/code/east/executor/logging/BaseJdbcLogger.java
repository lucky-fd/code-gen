/**
 * Copyright 2009-2020 the original author or authors.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yushi.code.east.executor.logging;

import java.lang.reflect.Method;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Base class for proxies to do logging.
 *
 * @author Clinton Begin
 * @author Eduardo Macarron
 */
public abstract class BaseJdbcLogger {

  protected static final Set<String> SET_METHODS;
  protected static final Set<String> EXECUTE_METHODS = new HashSet<>();

  private final Map<Object, Object> columnMap = new HashMap<>();

  private final List<Object> columnNames = new ArrayList<>();
  private final List<Object> columnValues = new ArrayList<>();

  protected final Log log;
  protected final int queryStack;

  /*
   * Default constructor
   */
  public BaseJdbcLogger(Log log, int queryStack) {
    this.log = log;
    if (queryStack == 0) {
      this.queryStack = 1;
    } else {
      this.queryStack = queryStack;
    }
  }

  static {
    SET_METHODS =
        Arrays.stream(PreparedStatement.class.getDeclaredMethods())
            .filter(method -> method.getName().startsWith("set"))
            .filter(method -> method.getParameterCount() > 1)
            .map(Method::getName)
            .collect(Collectors.toSet());

    EXECUTE_METHODS.add("execute");
    EXECUTE_METHODS.add("executeUpdate");
    EXECUTE_METHODS.add("executeQuery");
    EXECUTE_METHODS.add("addBatch");
  }

  protected void setColumn(Object key, Object value) {
    columnMap.put(key, value);
    columnNames.add(key);
    columnValues.add(value);
  }

  protected Object getColumn(Object key) {
    return columnMap.get(key);
  }

  protected String getParameterValueString() {
    List<Object> typeList = new ArrayList<>(columnValues.size());
    for (Object value : columnValues) {
      if (value == null) {
        typeList.add("null");
      } else {
        typeList.add(objectValueString(value) + "(" + value.getClass().getSimpleName() + ")");
      }
    }
    final String parameters = typeList.toString();
    return parameters.substring(1, parameters.length() - 1);
  }

  protected String objectValueString(Object value) {
    if (value instanceof Array) {
      try {
        final Object obj = ((Array) value).getArray();
        if (obj == null) {
          return "null";
        }
        final Class<?> clazz = obj.getClass();
        if (!clazz.isArray()) {
          return obj.toString();
        }
        final Class<?> componentType = obj.getClass().getComponentType();
        if (long.class.equals(componentType)) {
          return Arrays.toString((long[]) obj);
        } else if (int.class.equals(componentType)) {
          return Arrays.toString((int[]) obj);
        } else if (short.class.equals(componentType)) {
          return Arrays.toString((short[]) obj);
        } else if (char.class.equals(componentType)) {
          return Arrays.toString((char[]) obj);
        } else if (byte.class.equals(componentType)) {
          return Arrays.toString((byte[]) obj);
        } else if (boolean.class.equals(componentType)) {
          return Arrays.toString((boolean[]) obj);
        } else if (float.class.equals(componentType)) {
          return Arrays.toString((float[]) obj);
        } else if (double.class.equals(componentType)) {
          return Arrays.toString((double[]) obj);
        } else {
          return Arrays.toString((Object[]) obj);
        }
      } catch (SQLException e) {
        return value.toString();
      }
    }
    return value.toString();
  }

  protected String getColumnString() {
    return columnNames.toString();
  }

  protected void clearColumnInfo() {
    columnMap.clear();
    columnNames.clear();
    columnValues.clear();
  }

  protected String removeExtraWhitespace(String original) {
    StringTokenizer tokenizer = new StringTokenizer(original);
    StringBuilder builder = new StringBuilder();
    boolean hasMoreTokens = tokenizer.hasMoreTokens();
    while (hasMoreTokens) {
      builder.append(tokenizer.nextToken());
      hasMoreTokens = tokenizer.hasMoreTokens();
      if (hasMoreTokens) {
        builder.append(' ');
      }
    }
    return builder.toString();
  }

  protected boolean isDebugEnabled() {
    return log.isDebugEnabled();
  }

  protected boolean isTraceEnabled() {
    return log.isTraceEnabled();
  }

  protected void debug(String text, boolean input) {
    if (log.isDebugEnabled()) {
      log.debug(prefix(input) + text);
    }
  }

  protected void trace(String text, boolean input) {
    if (log.isTraceEnabled()) {
      log.trace(prefix(input) + text);
    }
  }

  private String prefix(boolean isInput) {
    char[] buffer = new char[queryStack * 2 + 2];
    Arrays.fill(buffer, '=');
    buffer[queryStack * 2 + 1] = ' ';
    if (isInput) {
      buffer[queryStack * 2] = '>';
    } else {
      buffer[0] = '<';
    }
    return new String(buffer);
  }
}
