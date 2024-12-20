package com.yushi.code.east.condition;

import com.yushi.code.east.function.GetterFunction;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 可用于指定一个操作包含/不包含的字段.
 *
 * @author fdong
 * @since 10/07/2021
 */
public class Fields<T> {
  private final Set<Field> includes = new LinkedHashSet<>();
  private final Set<Field> excludes = new LinkedHashSet<>();
  private final Class<T> clazz;

  private Fields(@Nonnull final Class<T> clazz) {
    this.clazz = clazz;
  }

  public static <T> Fields<T> of(@Nonnull final Class<T> clazz) {
    return new Fields<>(clazz);
  }

  @Nonnull
  public Class<T> getClazz() {
    return clazz;
  }

  @SafeVarargs
  public final Fields<T> include(@Nonnull final GetterFunction<T, ?>... includeFields) {
    for (final GetterFunction<T, ?> includeField : includeFields) {
      include(true, includeField);
    }
    return this;
  }

  public final Fields<T> include(final boolean include, final GetterFunction<T, ?> includeField) {
    if (include) {
      this.includes.add(includeField.getField());
    }
    return this;
  }

  @SafeVarargs
  public final Fields<T> exclude(@Nonnull final GetterFunction<T, ?>... excludeFields) {
    for (final GetterFunction<T, ?> excludeField : excludeFields) {
      exclude(true, excludeField);
    }
    return this;
  }

  public final Fields<T> exclude(
      final boolean exclude, @Nonnull final GetterFunction<T, ?> excludeField) {
    if (exclude) {
      this.includes.remove(excludeField.getField());
      this.excludes.add(excludeField.getField());
    }
    return this;
  }

  public Set<Field> getIncludeFields() {
    return includes.isEmpty() ? Collections.emptySet() : includes;
  }

  public Set<Field> getExcludeFields() {
    return excludes.isEmpty() ? Collections.emptySet() : excludes;
  }
}
