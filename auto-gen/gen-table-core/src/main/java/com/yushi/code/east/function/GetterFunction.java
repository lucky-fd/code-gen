package com.yushi.code.east.function;

import java.util.function.Function;

/**
 * @author fdong
 * @since 2019/12/26
 */
@FunctionalInterface
public interface GetterFunction<T, U> extends Function<T, U>, FieldFunction {}
