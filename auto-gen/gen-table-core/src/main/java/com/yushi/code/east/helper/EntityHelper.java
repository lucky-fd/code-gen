package com.yushi.code.east.helper;

import com.yushi.code.east.EastContext;
import com.yushi.code.east.annotation.TableInfo;
import com.yushi.code.east.config.EastConfiguration;
import com.yushi.code.east.executor.TableExporter;
import com.yushi.code.east.function.FieldFunction;
import com.yushi.code.east.mapping.EntityMapping;
import com.yushi.code.east.support.EntityColumn;
import com.yushi.code.east.support.EntityInfo;
import com.yushi.code.east.util.BeanUtils;
import com.yushi.code.east.util.CollectionUtils;
import com.yushi.code.east.util.EntityUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Entity helper.
 *
 * @author fdong
 * @since 2020 /5/12
 */
@Slf4j
public class EntityHelper {
  /** 实体信息:{类全路径:EntityInfo} */
  private static final Map<Class<?>, EntityInfo> CLAZZ_ENTITY_MAP = new ConcurrentHashMap<>();

  private static EastContext eastContext;

  public static void initial(final EastContext eastContext) {
    EntityHelper.eastContext = eastContext;
  }

  /**
   * 初始化实体和表对应信息.
   *
   * @param <T> the type parameter
   * @param clazz the clazz
   */
  public static <T> void initEntity(final Class<T> clazz) {
    final EntityInfo entityInfo =
        EntityInfo.of(
            clazz, eastContext.getEastConfiguration(), eastContext.getDbMetaData().getDialect());
    CLAZZ_ENTITY_MAP.put(clazz, entityInfo);
    // 表定义更新
    final EastConfiguration.DdlAuto ddlAuto = eastContext.getEastConfiguration().getDdlAuto();
    if (ddlAuto == null || EastConfiguration.DdlAuto.NONE.equals(ddlAuto)) {
      return;
    }
    if (!entityInfo.isMapToTable()) {
      return;
    }
    if (EastConfiguration.DdlAuto.CREATE.equals(ddlAuto)) {
      TableExporter.of(eastContext).createTable(entityInfo, true);
    } else if (EastConfiguration.DdlAuto.UPDATE.equals(ddlAuto)) {
      TableExporter.of(eastContext).updateTable(entityInfo);
    }
  }

  /** 初始化关系映射. */
  public static void initEntityMapping() {
    CLAZZ_ENTITY_MAP.values().stream()
        .filter(o -> o.getClazz().isAnnotationPresent(TableInfo.class))
        .forEach(EntityMapping::initial);
    EntityMapping.valid(CLAZZ_ENTITY_MAP);
  }

  /**
   * 获取lambda表达式对应的数据库表列名.
   *
   * @param function the function
   * @return the column
   */
  public static String getColumn(FieldFunction function) {
    if (log.isTraceEnabled()) {
      log.trace("getColumn:[{}]", CLAZZ_ENTITY_MAP);
    }
    if (eastContext == null || eastContext.getEastConfiguration() == null) {
      return EntityUtils.fieldToColumn(function.getField());
    }
    final EntityColumn entityColumn =
        initEntityIfNeeded(function.getImplClassFullPath())
            .getFieldColumnMap()
            .get(function.getField());
    return entityColumn == null ? null : entityColumn.getName();
  }

  /**
   * 获取Field对应的jdbcType名称,用于sql设置值.<br>
   * 默认使用{@link TableColumn#columnDefinition()}内的列定义,如果为空则使用默认值.
   *
   * @param field the field
   * @param defaultValue 默认值
   * @return jdbcType名称 jdbc type name
   * @see Types
   */
  public static String getJdbcTypeName(final Field field, final String defaultValue) {
    return Optional.of(getEntityInfo(field.getDeclaringClass()))
        .map(EntityInfo::getFieldColumnMap)
        .map(o -> o.get(field))
        .map(EntityColumn::getTypeName)
        .orElse(defaultValue);
  }

  /**
   * 获取Field对应的jdbc 数组名称.<br>
   * 默认使用{@link TableColumn#columnDefinition()}内的列定义,如果为空则使用默认值.
   *
   * @param field the field
   * @param defaultValue 默认值
   * @return jdbc数组类型名称
   * @see Types
   */
  public static String getJdbcArrayTypeName(final Field field, final String defaultValue) {
    final Optional<EntityInfo> entityInfo = Optional.of(getEntityInfo(field.getDeclaringClass()));
    final Optional<Map<Field, EntityColumn>> fieldEntityColumnMap =
        entityInfo.map(EntityInfo::getFieldColumnMap);
    final Optional<EntityColumn> entityColumn = fieldEntityColumnMap.map(o -> o.get(field));
    if (entityColumn.isPresent()) {
      return entityColumn.get().getArrayTypeName();
    }
    final Optional<Field> factField =
        entityInfo
            .map(EntityInfo::getColumnFieldMap)
            .map(o -> o.get(EntityUtils.fieldToColumn(field)));
    if (factField.isPresent()) {
      return fieldEntityColumnMap
          .map(o -> o.get(factField.get()))
          .map(EntityColumn::getArrayTypeName)
          .orElse(defaultValue);
    }
    return defaultValue;
  }

  @Nonnull
  public static <T> EntityInfo getEntityInfo(@Nonnull final Class<T> clazz) {
    return initEntityIfNeeded(clazz);
  }

  /**
   * 获取实体的主键字段.
   *
   * @param <T> the type parameter
   * @param clazz the clazz
   * @return the entity id field
   */
  public static <T> Field getEntityIdField(@Nonnull final Class<T> clazz) {
    return getEntityInfo(clazz).getIdColumn().getField();
  }

  private static <T> EntityInfo initEntityIfNeeded(@Nonnull final Class<T> clazz) {
    final String fullPath = clazz.getTypeName();
    synchronized (EntityHelper.class) {
      final EntityInfo entityInfo = CLAZZ_ENTITY_MAP.get(clazz);
      if (entityInfo == null || CollectionUtils.isEmpty(entityInfo.getFieldColumnMap())) {
        initEntity(clazz);
      }
    }
    return CLAZZ_ENTITY_MAP.get(clazz);
  }

  private static EntityInfo initEntityIfNeeded(final String fullPath) {
    final Class<?> clazz = BeanUtils.getClazz(fullPath);
    //noinspection SynchronizationOnLocalVariableOrMethodParameter
    synchronized (fullPath) {
      final EntityInfo entityInfo = CLAZZ_ENTITY_MAP.get(clazz);
      if (entityInfo == null || CollectionUtils.isEmpty(entityInfo.getFieldColumnMap())) {
        initEntity(clazz);
      }
    }
    return CLAZZ_ENTITY_MAP.get(clazz);
  }

  /**
   * 判断一个Class是否映射到数据库表.true:是
   *
   * <p>映射到数据库表需要实体包含注解: {@link TableInfo}.
   */
  public static boolean isMapToTable(final Class<?> clazz) {
    return clazz != null && clazz.isAnnotationPresent(TableInfo.class);
  }
}
