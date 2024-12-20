package com.yushi.code.east.util;

import com.yushi.code.east.EastContext;
import com.yushi.code.east.annotation.*;
import com.yushi.code.east.config.EastConfiguration;
import com.yushi.code.east.dialect.Dialect;
import com.yushi.code.east.domain.InterEnum;
import com.yushi.code.east.helper.EntityHelper;
import com.yushi.code.east.mapping.EntityMapping;
import com.yushi.code.east.support.EntityColumn;
import com.yushi.code.east.support.EntityInfo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * The type Bean utils.
 *
 * @author fdong
 * @since 2019 /12/26
 */
@Slf4j
public final class EntityUtils {
  private static EastConfiguration eastConfiguration;
  private static Dialect dialect;

  /** {@link BaseService} */
  private static final Map<Class<?>, WeakReference<Class<?>>> SERVICE_POJO_MAP =
      new ConcurrentHashMap<>();

  public static void initial(final EastContext context) {
    EntityUtils.eastConfiguration = context.getEastConfiguration();
    EntityUtils.dialect = context.getDbMetaData().getDialect();
  }

  public static List<Field> getAllColumnFields(@Nonnull final Class<?> obj) {
    return getAllColumnFields(obj, null);
  }

  /** TODO WARN 这里不应该包含关联属性，获取对象映射到数据库的属性,包括关系属性.<br> */
  public static List<Field> getAllColumnFields(
      @Nonnull final Class<?> obj, @Nullable SqlStatementType sqlStatementType) {
    Stream<Field> stream =
        BeanUtils.retrieveDeclaredFields(obj).stream().filter(EntityUtils::filterColumnField);
    if (sqlStatementType != null) {
      final EntityInfo entityInfo = EntityHelper.getEntityInfo(obj);
      final Map<Field, EntityColumn> fieldColumnMap = entityInfo.getFieldColumnMap();
      if (sqlStatementType.equals(SqlStatementType.INSERT)) {
        stream = stream.filter(o -> fieldColumnMap.get(o).isInsertable());
      } else if (sqlStatementType.equals(SqlStatementType.UPDATE)) {
        stream = stream.filter(o -> fieldColumnMap.get(o).isUpdatable());
      }
    }
    final List<Field> fields = stream.collect(toList());
    if (log.isTraceEnabled()) {
      log.trace("getAllColumnFields:[{}]", fields);
    }
    return fields;
  }

  /**
   * 列必须符合以下条件:
   * <li>非static
   * <li>非transient
   * <li>基本类型(对应的包装类型)<br>
   *
   *     <p>或 标记有注解({@link OneToOne},{@link OneToMany},{@link ManyToOne})中的一个 <br>
   *
   *     <p>或 List{@code <T>}/Set{@code <T>},T满足上一个条件
   */
  private static boolean filterColumnField(final Field field) {
    final int modifiers = field.getModifiers();
    // 不保存 static 和 transient 修饰的字段
    if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
      return false;
    }
    if (field.isAnnotationPresent(TableColumn.class)) {
      return true;
    }
    if (dialect.isSupportJavaType(field.getGenericType())) {
      return true;
    }
    if (field.getType().isEnum()) {
      return true;
    }
    return InterEnum.class.isAssignableFrom(field.getType());
  }

  public enum SqlStatementType {
    INSERT,
    DELETE,
    UPDATE,
    SELECT
  }

  /** 获取对象映射到数据库且值不为null的属性. */
  public static <T> List<Field> getNonNullColumnFields(
      @Nonnull final T t, final SqlStatementType sqlStatementType) {
    Stream<Field> stream =
        EntityHelper.getEntityInfo(t.getClass()).getEntityColumns().stream()
            .map(EntityColumn::getField)
            .filter(EntityUtils::filterColumnField);
    if (sqlStatementType != null) {
      final EntityInfo entityInfo = EntityHelper.getEntityInfo(t.getClass());
      final Map<Field, EntityColumn> fieldColumnMap = entityInfo.getFieldColumnMap();
      if (sqlStatementType.equals(SqlStatementType.INSERT)) {
        stream = stream.filter(o -> fieldColumnMap.get(o).isInsertable());
      } else if (sqlStatementType.equals(SqlStatementType.UPDATE)) {
        stream = stream.filter(o -> fieldColumnMap.get(o).isUpdatable());
      }
    }
    final List<Field> fields =
        stream.filter(field -> BeanUtils.getFieldValue(t, field) != null).collect(toList());
    if (log.isTraceEnabled()) {
      log.debug("getNonNullColumnFields:[{}]", fields);
    }
    return fields;
  }

  /**
   * 获取对象映射到数据库的列名.
   *
   * @see TableColumn#name() Column#name()
   * @see StringUtils#camelToUnderline(String) StringUtils#camelToUnderline(String)
   * @see Field#getName() Field#getName()
   */
  public static List<String> getAllColumns(
      @Nonnull final Class<?> obj, final SqlStatementType sqlStatementType) {
    final List<String> columns =
        getAllColumnFields(obj, sqlStatementType).stream()
            .map(EntityUtils::fieldToColumn)
            .collect(toList());
    log.debug("getAllColumn:[{}]", columns);
    return columns;
  }

  /**
   * 获取对象映射到数据库的非空属性的列名.<br>
   * 默认值为{@link TableColumn#name()};如果前者为空,值为对象属性名的下划线表示<br>
   * {@link StringUtils#camelToUnderline(String)},{@link Field#getName()}
   *
   * @param t the t
   * @return string non null columns
   * @see TableColumn#name() Column#name()
   * @see StringUtils#camelToUnderline(String) StringUtils#camelToUnderline(String)
   * @see Field#getName() Field#getName()
   */
  public static List<String> getNonNullColumns(
      @Nonnull final Object t, final SqlStatementType sqlStatementType) {
    final List<String> columns =
        getNonNullColumnFields(t, sqlStatementType).stream()
            .map(EntityUtils::fieldToColumn)
            .collect(toList());
    log.debug("getNonNullColumns:[{}]", columns);
    return columns;
  }

  /**
   * 获取对象映射到数据库的非空属性的列名,以逗号分割.<br>
   * 默认值为{@link TableColumn#name()};如果前者为空,值为对象属性名的下划线表示<br>
   * {@link StringUtils#camelToUnderline(String)},{@link Field#getName()}
   *
   * @param t the t
   * @return string non null column
   * @see TableColumn#name() Column#name()
   * @see StringUtils#camelToUnderline(String)
   * @see Field#getName() Field#getName()
   * @see #getNonNullColumns(Object,SqlStatementType)
   */
  public static String getNonNullColumn(
      @Nonnull final Object t, final SqlStatementType sqlStatementType) {
    final String nonNullColumn = String.join(",", getNonNullColumns(t, sqlStatementType));
    log.debug("getNonNullColumn:[{}]", nonNullColumn);
    return nonNullColumn;
  }

  /** {@link #fieldToColumn(Field, boolean)} */
  public static String fieldToColumn(@Nonnull final Field field) {
    return fieldToColumn(field, false);
  }

  /**
   * 获取对象属性对应的数据库列名.<br>
   *
   * @param field the field
   * @param depth 是否解析关联对象
   * @return string string
   * @see TableColumn#name() TableColumn#name()
   * @see StringUtils#camelToUnderline(String) StringUtils#camelToUnderline(String)
   * @see Field#getName() Field#getName()
   */
  public static String fieldToColumn(@Nonnull final Field field, final boolean depth) {
    final Class<?> fieldType = field.getType();
    // 普通字段判断TableColumn注解
    if (!depth || field.getType().getClassLoader() == null || !EntityMapping.MappingInfo.isValidMapping(field)) {
      final TableColumn column = field.getAnnotation(TableColumn.class);
      return column != null && !"".equals(column.name())
          ? column.name()
          : StringUtils.camelToUnderline(field.getName());
    }
    // 关联字段
    final OneToOne oneToOne = field.getAnnotation(OneToOne.class);
    final String joinFieldStr;
    final String targetFieldStr;
    if (oneToOne != null) {
      joinFieldStr = oneToOne.joinField();
      targetFieldStr = oneToOne.targetField();
    } else {
      final ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
      if (manyToOne != null) {
        joinFieldStr = manyToOne.joinField();
        targetFieldStr = manyToOne.targetField();
      } else {
        return null;
      }
    }
    // 手动指定关联字段名
    if (!"".equals(joinFieldStr)) {
      final Field joinField = BeanUtils.getDeclaredField(fieldType, joinFieldStr);
      if (joinField == null) {
        throw new IllegalArgumentException(
            String.format(
                "%s %s's join field could not null", fieldType.getName(), field.getName()));
      }
      return fieldToColumn(joinField, false);
    }
    // 关联主键
    final EntityInfo entityInfo = EntityHelper.getEntityInfo(fieldType);
    if ("".equals(targetFieldStr)) {
      final String primaryKeys =
          entityInfo.getPrimaryKeys().stream().map(EntityColumn::getName).collect(joining("_"));
      return StringUtils.camelToUnderline(entityInfo.getName().concat("_").concat(primaryKeys));
    }
    final Field targetField =
        entityInfo.getFieldColumnMap().keySet().stream()
            .filter(f -> f.getName().equals(targetFieldStr))
            .findFirst()
            .orElse(null);
    if (targetField == null) {
      throw new IllegalArgumentException(
          String.format("Invalid target field %s for %s", targetFieldStr, fieldType));
    }
    final String column = fieldToColumn(targetField, false);
    return column == null
        ? null
        : StringUtils.camelToUnderline(entityInfo.getName().concat("_").concat(column));
  }

  /**
   * 表名: {@link TableInfo#name()} &gt; 类名(驼封转下划线)
   *
   * @param clazz the clazz
   * @return the table name
   */
  public static String getTableName(final EastConfiguration eastConfiguration, final Class<?> clazz) {
    final TableInfo tableInfo = clazz.getAnnotation(TableInfo.class);
    if (tableInfo != null && StringUtils.nonEmpty(tableInfo.name())) {
      return tableInfo.name();
    }
    return eastConfiguration.getGlobalTablePrefix()
        + StringUtils.camelToUnderline(clazz.getSimpleName());
  }
}
