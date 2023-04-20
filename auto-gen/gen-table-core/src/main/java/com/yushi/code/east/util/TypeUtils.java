package com.yushi.code.east.util;


import com.yushi.code.east.reflect.ParameterizedTypeImpl;

import java.lang.reflect.Type;

/**
 * The type Type utils.
 *
 * @author fdong
 * @since 2020.08.21
 */
public class TypeUtils {
  public static Type getType(final Class<?> rawType, final Class<?> actualArgumentType) {
    return new ParameterizedTypeImpl(rawType, new Type[] {actualArgumentType}, null);
  }
}
