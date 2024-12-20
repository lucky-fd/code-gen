package com.yushi.code.east.function;

import com.yushi.code.east.helper.EntityHelper;
import com.yushi.code.east.util.BeanUtils;
import com.yushi.code.east.util.LambdaUtils;
import com.yushi.code.east.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于bean属性setter {@link SetterFunction}/getter {@link GetterFunction}方法 函数式接口
 *
 * @author fdong
 * @since 2020 5/5
 * @see SetterFunction
 * @see GetterFunction
 */
public interface FieldFunction extends Serializable {
  Logger log = LoggerFactory.getLogger(FieldFunction.class);
  Map<FieldFunction, WeakReference<Field>> LAMBDA_FIELD_MAP = new ConcurrentHashMap<>();

  /**
   * 获取实现类全路径.
   *
   * @return the impl class
   */
  default String getImplClassFullPath() {
    return LambdaUtils.getImplClassFullPath(this);
  }

  /**
   * 获取实现类名.
   *
   * @return the impl class
   */
  default String getImplClassName() {
    return LambdaUtils.getImplClassName(this);
  }

  /**
   * 获取对应的Field.
   *
   * @return the field
   */
  default Field getField() {
    return Optional.ofNullable(LAMBDA_FIELD_MAP.get(this))
        .map(Reference::get)
        .orElseGet(
            () -> {
              final SerializedLambda lambda = LambdaUtils.serializedLambda(this);
              final String methodName = lambda.getImplMethodName();
              final String classPath = getImplClassFullPath();
              final String property = BeanUtils.methodToProperty(methodName);
              Field field;
              try {
                field = BeanUtils.getClazz(classPath).getDeclaredField(property);
                LAMBDA_FIELD_MAP.put(this, new WeakReference<>(field));
              } catch (Exception ignored) {
                try {
                  field =
                      BeanUtils.getClazz(classPath)
                          .getDeclaredField("is" + StringUtils.firstUppercase(property));
                  LAMBDA_FIELD_MAP.put(this, new WeakReference<>(field));
                } catch (Exception e) {
                  log.warn(
                      "getField:cannot get field from lambda[{},{}]",
                      e.getMessage(),
                      e.getMessage());
                  log.error(e.getMessage(), e);
                  throw new IllegalArgumentException(e.getMessage(), e);
                }
              }
              return field;
            });
  }

  /**
   * 获取Field的泛型参数类型.
   *
   * @return the generic type
   */
  default Type getGenericType() {
    return getField().getGenericType();
  }

  /**
   * 获取Field的泛型参数泛型类型.
   *
   * @return the type [ ]
   */
  default Type[] getGenericTypeArgumentTypes() {
    return ((ParameterizedType) getGenericType()).getActualTypeArguments();
  }

  /**
   * 获取lambda表达式对应的数据库表列名.
   *
   * @return the column
   */
  default String getColumn() {
    return EntityHelper.getColumn(this);
  }
}
