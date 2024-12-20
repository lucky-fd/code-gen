package com.yushi.code.east.mapping;

import com.yushi.code.east.annotation.ManyToOne;
import com.yushi.code.east.annotation.OneToMany;
import com.yushi.code.east.annotation.OneToOne;
import com.yushi.code.east.condition.StringCnd;
import com.yushi.code.east.executor.Dao;
import com.yushi.code.east.helper.EntityHelper;
import com.yushi.code.east.util.BeanUtils;
import com.yushi.code.east.util.EntityUtils;
import com.yushi.code.east.util.StringUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * The enum Mapping type.
 *
 * <pre>
 * {@code
 *  public class F {
 *    @OneToOne
 *    private Foo foo;
 *
 *    @OneToMany
 *    private List<Foo> foos;
 *
 *    @ManyToOne
 *    private Foo foo;
 *  }
 * }
 *
 * @author fdong
 * @since 2020.09.19
 */
public enum MappingType {
  ONE_TO_ONE {
    @Override
    public <E> Object fetchMapping(final E poJo, final EntityMapping.MappingInfo mappingInfo, final Dao dao) {
      final Field joinField = mappingInfo.getJoinField();
      final Object relationValue = BeanUtils.getFieldValueIgnoreException(poJo, joinField);
      final Class<?> targetClazz = mappingInfo.getTargetClazz();
      return relationValue == null
          ? null
          : dao.fetchOne(
          StringCnd.of(targetClazz).eq(mappingInfo.getTargetColumn(), relationValue));
    }

    @Override
    EntityMapping.MappingInfo populateMappingInfo(final Field field) {
      final EntityMapping.MappingInfo mappingInfo = new EntityMapping.MappingInfo();
      mappingInfo.setMappingType(this);
      final Class<?> clazz = field.getDeclaringClass();
      mappingInfo.setClazz(clazz);
      mappingInfo.setField(field);
      final Class<?> targetClazz = field.getType();
      mappingInfo.setTargetClazz(targetClazz);

      final OneToOne oneToOne = field.getAnnotation(OneToOne.class);
      final String targetFieldStr = oneToOne.targetField();
      final Field targetField;
      final Field idField = EntityHelper.getEntityIdField(targetClazz);
      if (idField == null) {
        throw new IllegalStateException(String.format("No id defined in %s", targetClazz));
      }
      if (!"".equals(targetFieldStr)) {
        targetField = BeanUtils.getDeclaredField(mappingInfo.getTargetClazz(), targetFieldStr);
      } else {
        targetField = idField;
      }
      if (targetField == null) {
        throw new IllegalArgumentException(
            String.format("%s %s's target field could not null", clazz.getName(), field.getName()));
      }
      mappingInfo.setTargetField(targetField);
      mappingInfo.setTargetColumn(EntityUtils.fieldToColumn(targetField, false));
      final String joinFieldStr =
          !"".equals(oneToOne.joinField())
              ? oneToOne.joinField()
              : (StringUtils.firstLowercase(targetClazz.getSimpleName())
                  + StringUtils.firstUppercase(targetField.getName()));
      final Field joinField = BeanUtils.getDeclaredField(clazz, joinFieldStr);
      mappingInfo.setJoinField(joinField);
      if (joinField == null) {
        throw new IllegalArgumentException(
            String.format(
                "Error to parse %s,required field [%s] in %s", field, joinFieldStr, clazz));
      }
      mappingInfo.setJoinColumn(
          EntityHelper.getEntityIdField(targetClazz).getName()
              + "_"
              + mappingInfo.getTargetColumn());
      return mappingInfo;
    }
  },
  ONE_TO_MANY {
    @Override
    public <E> Object fetchMapping(final E poJo, final EntityMapping.MappingInfo mappingInfo, final Dao dao) {
      final Class<?> targetClazz = mappingInfo.getTargetClazz();
      final Object relationValue = BeanUtils.getFieldValueIgnoreException(poJo, mappingInfo.getJoinField());
      return relationValue == null
          ? null
          : dao.fetchAll(
          StringCnd.of(targetClazz).eq(mappingInfo.getTargetColumn(), relationValue));
    }

    @Override
    EntityMapping.MappingInfo populateMappingInfo(final Field field) {
      final EntityMapping.MappingInfo mappingInfo = new EntityMapping.MappingInfo();
      mappingInfo.setMappingType(this);
      final Class<?> clazz = field.getDeclaringClass();
      mappingInfo.setClazz(clazz);
      mappingInfo.setField(field);
      final Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
      final Class<?> manyClazz = (Class<?>) type;
      mappingInfo.setTargetClazz(manyClazz);

      final OneToMany oneToMany = field.getAnnotation(OneToMany.class);
      String manyFieldStr = oneToMany.targetField();
      final Field manyIdField = EntityHelper.getEntityIdField(manyClazz);
      if (manyIdField == null) {
        throw new IllegalStateException(String.format("No id defined in %s", manyClazz));
      }
      Field manyField = BeanUtils.getDeclaredField(manyClazz, manyFieldStr);
      if ("".equals(manyFieldStr) || manyField == null) {
        throw new IllegalArgumentException(
            String.format("%s %s's target field could not null", clazz.getName(), field.getName()));
      }
      // 可能是private long oneId,private One one;
      final ManyToOne manyToOne = manyField.getAnnotation(ManyToOne.class);
      // 当前类被关联字段
      {
        Field joinField =
            manyToOne == null || "".equals(manyToOne.targetField())
                ? EntityHelper.getEntityIdField(clazz)
                : BeanUtils.getDeclaredField(clazz, manyToOne.targetField());
        if (joinField == null) {
          throw new IllegalArgumentException(
              String.format(
                  "Target field not found %s in %s", manyField.getName(), manyClazz.getName()));
        }
        mappingInfo.setJoinField(joinField);
        mappingInfo.setJoinColumn(EntityUtils.fieldToColumn(joinField, false));
      }
      // 解析目标字段,分为普通类型和引用类型两种情况
      if (manyField.getType().getClassLoader() == null) {
        mappingInfo.setTargetField(manyField);
        mappingInfo.setTargetColumn(EntityUtils.fieldToColumn(manyField, false));
      } else {
        if (manyToOne == null) {
          throw new IllegalArgumentException(
              String.format("%s should annotated with ManyToOne", manyIdField.getName()));
        }
        final String joinFieldStr =
            !"".equals(manyToOne.joinField())
                ? manyToOne.joinField()
                : StringUtils.firstLowercase(clazz.getSimpleName())
                    + StringUtils.firstUppercase(mappingInfo.getJoinField().getName());
        final Field joinField = BeanUtils.getDeclaredField(manyClazz, joinFieldStr);
        if (joinField == null) {
          throw new IllegalArgumentException(
              String.format(
                  "Error to parse %s,could not found field %s in %s",
                  field, joinFieldStr, manyClazz));
        }
        mappingInfo.setTargetField(joinField);
        mappingInfo.setTargetColumn(EntityUtils.fieldToColumn(joinField, false));
      }
      return mappingInfo;
    }
  },
  /** The Many to one. */
  MANY_TO_ONE {
    @Override
    public <E> Object fetchMapping(final E poJo, final EntityMapping.MappingInfo mappingInfo, final Dao dao) {
      final Field joinField = mappingInfo.getJoinField();
      final Class<?> targetClazz = mappingInfo.getTargetClazz();
      final Object relationValue = BeanUtils.getFieldValueIgnoreException(poJo, joinField);
      return relationValue == null
          ? null
          : dao.fetchOne(
          StringCnd.of(targetClazz).eq(mappingInfo.getTargetField(), relationValue));
    }

    @Override
    EntityMapping.MappingInfo populateMappingInfo(final Field field) {
      final EntityMapping.MappingInfo mappingInfo = new EntityMapping.MappingInfo();
      mappingInfo.setMappingType(this);
      final Class<?> clazz = field.getDeclaringClass();
      mappingInfo.setClazz(clazz);
      mappingInfo.setField(field);
      final Class<?> targetClazz = field.getType();
      mappingInfo.setTargetClazz(targetClazz);

      final ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
      final String targetFieldStr = manyToOne.targetField();
      final Field targetField;
      final Field idField = EntityHelper.getEntityIdField(targetClazz);
      if (idField == null) {
        throw new IllegalArgumentException(
            String.format("no primary key defined in %s", targetClazz));
      }
      if (!"".equals(targetFieldStr)) {
        targetField = BeanUtils.getDeclaredField(mappingInfo.getTargetClazz(), targetFieldStr);
      } else {
        targetField = idField;
      }
      if (targetField == null) {
        throw new IllegalArgumentException(
            String.format("%s %s's target field could not null", clazz.getName(), field.getName()));
      }
      mappingInfo.setTargetField(targetField);
      mappingInfo.setTargetColumn(EntityUtils.fieldToColumn(targetField, false));
      final String joinFieldStr =
          !"".equals(manyToOne.joinField())
              ? manyToOne.joinField()
              : StringUtils.firstLowercase(targetClazz.getSimpleName())
                  + StringUtils.firstUppercase(targetField.getName());
      final Field joinField = BeanUtils.getDeclaredField(clazz, joinFieldStr);
      if (joinField == null) {
        throw new IllegalArgumentException(
            String.format(
                "Error to parse %s,could not found field %s in %s", field, joinFieldStr, clazz));
      }
      mappingInfo.setJoinField(joinField);
      mappingInfo.setJoinColumn(
          EntityHelper.getEntityIdField(targetClazz).getName()
              + "_"
              + mappingInfo.getTargetColumn());
      return mappingInfo;
    }
  },

  // /** The Many to many. */
  // MANY_TO_MANY {
  //   @Override
  //   public <T> T fetchMapping(
  //       final E obj, final MappingInfo mappingInfo, final Object relationValue)
  // {
  //     throw new CommonException("方法不支持");
  //   }
  // },
  /** The None. */
  NONE {
    @Override
    public <E> Object fetchMapping(final E poJo, final EntityMapping.MappingInfo mappingInfo, final Dao dao) {
      return null;
    }

    @Override
    EntityMapping.MappingInfo populateMappingInfo(final Field field) {
      return null;
    }
  },
  ;

  public abstract <E> Object fetchMapping(
          final E poJo, final EntityMapping.MappingInfo mappingInfo, final Dao dao);

  abstract EntityMapping.MappingInfo populateMappingInfo(final Field field);

  public static EntityMapping.MappingInfo getMappingInfo(final Field field) {
    return MappingType.of(field).populateMappingInfo(field);
  }

  public static @Nonnull MappingType of(final Field field) {
    if (field.isAnnotationPresent(OneToOne.class)) {
      return ONE_TO_ONE;
    }
    if (field.isAnnotationPresent(OneToMany.class)) {
      return ONE_TO_MANY;
    }
    if (field.isAnnotationPresent(ManyToOne.class)) {
      return MANY_TO_ONE;
    }
    /* 后续可能会支持
    if (field.isAnnotationPresent(ManyToMany.class)) {
      throw new CommonException("方法不支持");
      return MANY_TO_MANY;
    }*/
    return NONE;
  }
}
