package com.yushi.code.east.util;

import com.yushi.code.east.exception.WindException;
import com.yushi.code.east.function.FieldFunction;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Lambda utils.
 *
 * @author fdong
 * @since 2019 /12/26
 */
@Slf4j
public final class LambdaUtils {
  /** SerializedLambda 反序列化缓存 */
  private static final Map<Class<?>, WeakReference<SerializedLambda>> LAMBDA_MAP =
      new ConcurrentHashMap<>();

  /**
   * 获取lambda表达式对应的方法名.
   *
   * @param fieldFunction 需要解析的 lambda 对象
   * @return 返回解析后的结果 method name
   * @see LambdaUtils#serializedLambda(FieldFunction) LambdaUtils#serializedLambda(BeanFunction)
   */
  public static String getMethodName(FieldFunction fieldFunction) {
    return serializedLambda(fieldFunction).getImplMethodName();
  }

  /**
   * 获取lambda表达式对应的方法引用类名全路径.
   *
   * @param fieldFunction 需要解析的 lambda 对象
   * @return 返回解析后的结果 method name
   * @see LambdaUtils#serializedLambda(FieldFunction) LambdaUtils#serializedLambda(BeanFunction)
   */
  public static String getImplClassFullPath(FieldFunction fieldFunction) {
    return serializedLambda(fieldFunction).getImplClass().replaceAll("/", ".");
  }

  /**
   * 获取lambda表达式对应的方法引用类名.
   *
   * @param fieldFunction 需要解析的 lambda 对象
   * @return 返回解析后的结果 method name
   * @see LambdaUtils#serializedLambda(FieldFunction) LambdaUtils#serializedLambda(BeanFunction)
   */
  public static String getImplClassName(FieldFunction fieldFunction) {
    final String implClass = serializedLambda(fieldFunction).getImplClass().replaceAll("/", ".");
    return implClass.substring(implClass.lastIndexOf(".") + 1);
  }

  /**
   * Serialized beanFunction serialized beanFunction.
   *
   * @param fieldFunction the beanFunction
   * @return the serialized beanFunction
   */
  public static SerializedLambda serializedLambda(FieldFunction fieldFunction) {
    if (!fieldFunction.getClass().isSynthetic()) {
      throw new WindException("不支持非lambda表达式");
    }
    return Optional.ofNullable(LAMBDA_MAP.get(fieldFunction.getClass()))
        .map(WeakReference::get)
        .orElseGet(() -> getSerializedLambda(fieldFunction));
  }

  private static SerializedLambda getSerializedLambda(final FieldFunction fieldFunction) {
    try {
      final Class<? extends FieldFunction> clazz = fieldFunction.getClass();
      final Method writeReplace = clazz.getDeclaredMethod("writeReplace");
      if (!writeReplace.isAccessible()) {
        writeReplace.setAccessible(true);
      }
      final SerializedLambda serializedLambda =
          (SerializedLambda) writeReplace.invoke(fieldFunction);
      LAMBDA_MAP.put(clazz, new WeakReference<>(serializedLambda));
      return serializedLambda;
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new WindException(e);
    }
  }
}
