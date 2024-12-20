package com.yushi.code.east.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The type Collection utils.
 *
 * @author fdong
 * @since 2019 /12/26
 */
@SuppressWarnings("unused")
public class CollectionUtils {

  /**
   * Is empty boolean.
   *
   * @param collection the collection
   * @return the boolean
   */
  public static boolean isEmpty(@Nullable Collection<?> collection) {
    return (collection == null || collection.isEmpty());
  }

  /**
   * Is empty boolean.
   *
   * @param map the map
   * @return the boolean
   */
  public static boolean isEmpty(@Nullable Map<?, ?> map) {
    return (map == null || map.isEmpty());
  }

  /**
   * Non empty boolean.
   *
   * @param collection the collection
   * @return the boolean
   */
  public static boolean nonEmpty(@Nullable Collection<?> collection) {
    return !isEmpty(collection);
  }

  /**
   * Non empty boolean.
   *
   * @param map the map
   * @return the boolean
   */
  public static boolean nonEmpty(@Nullable Map<?, ?> map) {
    return !isEmpty(map);
  }

  /**
   * Do if non empty.
   *
   * @param collection the collection
   * @param consumer the consumer
   */
  public static <E> void doIfNonEmpty(
      @Nullable Collection<E> collection, @Nonnull Consumer<Collection<E>> consumer) {
    if (nonEmpty(collection)) {
      consumer.accept(collection);
    }
  }

  /**
   * 转换 {@link List} 对象,可选过滤.
   *
   * @param <T> the type parameter
   * @param <R> the type parameter
   * @param list the list
   * @param mapFunction the map function
   * @param filterFunction the filter function
   * @return the list
   */
  public static <T, R> List<R> toList(
      final List<T> list,
      @Nonnull final Function<T, R> mapFunction,
      final Predicate<R> filterFunction) {
    Objects.requireNonNull(mapFunction);
    return filterFunction == null
        ? list.stream().map(mapFunction).collect(Collectors.toList())
        : list.stream().map(mapFunction).filter(filterFunction).collect(Collectors.toList());
  }
}
