package com.yushi.code.east.support;


import com.yushi.code.east.annotation.TableColumn;
import com.yushi.code.east.annotation.TableId;
import com.yushi.code.east.dialect.Dialect;
import com.yushi.code.east.dialect.identity.IdentityColumnSupport;
import com.yushi.code.east.domain.InterEnum;
import com.yushi.code.east.exception.WindException;
import com.yushi.code.east.mapping.EntityMapping;
import com.yushi.code.east.util.EntityUtils;
import com.yushi.code.east.util.NumberUtils;
import com.yushi.code.east.util.StringUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

/**
 * 实体列信息(字段,sqlType).
 *
 * @author fdong
 * @since 13/08/2020
 */
@Data
public class EntityColumn {
    /**
     * 非浮点数长度.
     */
    public static final int DEFAULT_LENGTH = 255;

    /**
     * 浮点数长度.
     */
    public static final int DEFAULT_PRECISION = 19;

    /**
     * 小数位数.
     */
    public static final int DEFAULT_SCALE = 2;

    /**
     * 列名.
     */
    private String name;

    /**
     * 字段.
     */
    private Field field;

    /**
     * 对应java类型.
     */
    private Type type;

    /**
     * sql类型名.
     */
    private String typeName;

    /**
     * jdbc数组类型名.
     */
    private String arrayTypeName;

    /**
     * 长度.
     */
    private int length = DEFAULT_LENGTH;

    /**
     * Numeric类型长度.
     */
    private int precision = DEFAULT_PRECISION;

    /**
     * 精度,小数点位数.
     */
    private int scale = DEFAULT_SCALE;

    /**
     * 是否支持 INSERT 语句.为false时,保存会跳过该值.
     */
    private boolean insertable = true;

    /**
     * 是否支持 UPDATE 语句.为false时,更新会跳过该值.
     */
    private boolean updatable = true;

    /**
     * 是否可为空.true:可为空.
     */
    private boolean nullable = true;

    /**
     * 是否唯一.true:唯一.
     */
    private boolean unique = false;

    /**
     * 备注.
     */
    private String comment;

    /**
     * 忽略字段.
     */
    private boolean ignore;

    /**
     * 默认值.
     */
    private String defaultValue;

    /**
     * 是否是主键.
     */
    private boolean primaryKey = false;

    /**
     * 表字段顺序
     */
    private int sort = 1;

    /**
     * {@link TableColumn#columnDefinition()}.
     */
    @Getter(AccessLevel.NONE)
    private String columnDefinition;

    /**
     * 数据库是否支持列类型. 如果不支持,ddl时会跳过该字段.
     */
    private boolean supported;

    public String getColumnDdl(final Dialect dialect) {
        return name + " " + columnDefinition;
    }

    public String getCommentOnColumnString(final String tableName, final Dialect dialect) {
        return dialect.getCommentOnColumnString(tableName, name, comment);
    }

    /**
     * 获取唯一定义sql.
     */
    public String getUniqueDefinition() {
        return unique ? String.format("create unique index %s_index on table(%s)", name, name) : null;
    }

    private static final String defaultRegex = "default[ ]+[']?[\\w\\u4e00-\\u9fa5]+[']?[ ]?";
    private static final String commentRegex = "comment.*";

    public static EntityColumn of(
            @Nonnull Field field, Dialect dialect, final IdGenerator idGenerator) {
        // 如果是一对多关联的情况,不记录列信息
        if (EntityMapping.MappingInfo.isManyMapping(field)) {
            return null;
        }
        EntityColumn entityColumn = new EntityColumn();
        entityColumn.field = field;
        entityColumn.name = EntityUtils.fieldToColumn(field);
        entityColumn.type = field.getGenericType();

        final TableColumn tableColumn = field.getAnnotation(TableColumn.class);
        if (dialect.isSupportJavaType(entityColumn.type)
                || (entityColumn.type instanceof Class
                && InterEnum.class.isAssignableFrom((Class<?>) entityColumn.type))
                || (entityColumn.type instanceof Class && ((Class<?>) entityColumn.type).isEnum())
                || tableColumn != null) {
            entityColumn.supported = true;
        }
        if (field.isAnnotationPresent(TableId.class)) {
            entityColumn.primaryKey = true;
        }
        // 如果是基本类型,列定义不能为空
        if (entityColumn.getType() instanceof Class
                && ((Class<?>) entityColumn.getType()).isPrimitive()) {
            entityColumn.nullable = false;
        }
        if (tableColumn == null) {
            entityColumn.typeName =
                    entityColumn.supported
                            ? dialect.getTypeName(
                            entityColumn.getColumnDefinitionJavaType(field),
                            entityColumn.length,
                            entityColumn.precision,
                            entityColumn.scale)
                            : null;
        } else {
            entityColumn.ignore = tableColumn.ignore();
            entityColumn.comment = tableColumn.comment();
            entityColumn.sort = tableColumn.sort();
            if (!tableColumn.defaultValue().isEmpty()) {
                entityColumn.defaultValue = tableColumn.defaultValue();
            } else if (tableColumn.defaultBlankValue()) {
                entityColumn.defaultValue = "''";
            }
            // StringUtils.doIfNonEmpty(tableColumn.name(), name -> entityColumn.name = name);
            StringUtils.doIfNonEmpty(
                    tableColumn.columnDefinition(),
                    columnDefinition -> {
                        columnDefinition = columnDefinition.toLowerCase();
                        entityColumn.columnDefinition = columnDefinition;
                        String defaultValue = entityColumn.defaultValue;
                        if (StringUtils.nonEmpty(defaultValue)) {
                            String replacement =
                                    " default '" + (defaultValue.equals("''") ? "" : defaultValue) + "'";
                            entityColumn.columnDefinition =
                                    entityColumn.columnDefinition.replaceAll(defaultRegex, replacement);
                            if (!entityColumn.columnDefinition.contains("default")) {
                                entityColumn.columnDefinition += replacement;
                            }
                        }
                        String comment = entityColumn.comment;
                        if (StringUtils.nonEmpty(comment)) {
                            String replacement = " comment '" + comment + "'";
                            entityColumn.columnDefinition =
                                    entityColumn.columnDefinition.replaceAll(commentRegex, replacement);
                            if (!dialect.isSupportCommentOn()
                                    && !entityColumn.columnDefinition.contains("comment")) {
                                entityColumn.columnDefinition += replacement;
                            }
                        }
                    });
            entityColumn.arrayTypeName = tableColumn.arrayType();
            entityColumn.length = tableColumn.length();
            // 使用默认值而不是0
            NumberUtils.doIfGreaterThanZero(tableColumn.precision(), o -> entityColumn.precision = o);
            NumberUtils.doIfGreaterThanZero(tableColumn.scale(), o -> entityColumn.scale = o);
            entityColumn.insertable = tableColumn.insertable();
            entityColumn.updatable = tableColumn.updatable();
            entityColumn.nullable = entityColumn.nullable && tableColumn.nullable();
            entityColumn.unique = tableColumn.unique();

            if (entityColumn.columnDefinition == null) {
                entityColumn.typeName =
                        entityColumn.supported
                                ? dialect.getTypeName(
                                entityColumn.getColumnDefinitionJavaType(field),
                                entityColumn.length,
                                entityColumn.precision,
                                entityColumn.scale)
                                : null;
                if (entityColumn.typeName != null && entityColumn.arrayTypeName.isEmpty()) {
                    final String typeName = entityColumn.typeName;
                    final String definition =
                            typeName.contains(" ") ? typeName.substring(0, typeName.indexOf(" ")) : typeName;
                    final String leftParenthesis = "(";
                    final String leftSquareBracket = "[";
                    if (definition.contains(leftParenthesis)) {
                        entityColumn.arrayTypeName =
                                definition.substring(0, definition.indexOf(leftParenthesis));
                    } else if (definition.contains(leftSquareBracket)) {
                        entityColumn.arrayTypeName =
                                definition.substring(0, definition.indexOf(leftSquareBracket));
                    } else {
                        entityColumn.arrayTypeName = definition;
                    }
                }
            } //
            else if (entityColumn.typeName == null) {
                final String columnDefinition = entityColumn.columnDefinition;
                if (StringUtils.nonEmpty(columnDefinition)) {
                    final String trim = StringUtils.trimWhitespace(columnDefinition);
                    String definition = trim.contains(" ") ? trim.substring(0, trim.indexOf(" ")) : trim;
                    final String leftParenthesis = "(";
                    final String leftSquareBracket = "[";
                    if (definition.contains(leftParenthesis)) {
                        entityColumn.typeName = definition.substring(0, definition.indexOf(leftParenthesis));
                    } else if (definition.contains(leftSquareBracket)) {
                        entityColumn.typeName = definition.substring(0, definition.indexOf(leftSquareBracket));
                    } else {
                        entityColumn.typeName = definition;
                    }
                }
                if (entityColumn.arrayTypeName.isEmpty()) {
                    entityColumn.arrayTypeName = entityColumn.typeName;
                }
            }
        }
        entityColumn.columnDefinition = entityColumn.getColumnDefinition(dialect);
        // 默认主键定义
        if (entityColumn.isPrimaryKey()) {
            entityColumn.columnDefinition = getPrimaryKeyDefinition(dialect, entityColumn, idGenerator);
        }
        return entityColumn;
    }

    /**
     * 获取映射到数据库列的java字段类型
     */
    private Type getColumnDefinitionJavaType(Field field) {
        if (this.type instanceof Class) {
            final Class<?> clazz = (Class<?>) this.type;
            if (InterEnum.class.isAssignableFrom(clazz)) {
                final Object[] enumConstants = clazz.getEnumConstants();
                if (enumConstants != null && enumConstants.length > 0) {
                    @SuppressWarnings("rawtypes") final InterEnum interEnum = (InterEnum) enumConstants[0];
                    return interEnum.value().getClass();
                }

                Deque<Type> interfaceDeque =
                        new ArrayDeque<>(Arrays.asList(field.getType().getGenericInterfaces()));
                do {
                    Type current = interfaceDeque.pop();
                    if (current instanceof ParameterizedType
                            && ((ParameterizedType) current).getRawType().equals(InterEnum.class)) {
                        return ((ParameterizedType) current).getActualTypeArguments()[0];
                    }
                    if (current instanceof ParameterizedType) {
                        final Type rawType = ((ParameterizedType) current).getRawType();
                        if (rawType instanceof Class) {
                            Type[] subInterfaces = ((Class<?>) rawType).getGenericInterfaces();
                            for (Type anInterface : subInterfaces) {
                                interfaceDeque.push(anInterface);
                            }
                        }
                    }
                    if (current instanceof Class) {
                        Type[] subInterfaces = ((Class<?>) current).getGenericInterfaces();
                        for (Type anInterface : subInterfaces) {
                            interfaceDeque.push(anInterface);
                        }
                    }
                } while (!interfaceDeque.isEmpty());
                throw new WindException("could not reference generic type for: " + field.getType());
            }
            // 枚举 默认使用name,返回String
            else if (clazz.isEnum()) {
                return String.class;
            }
        }
        // 泛型限定参数
        else if (this.type instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) this.type;
            final Type[] bounds = typeVariable.getBounds();
            for (Type bound : bounds) {
                if (bound instanceof ParameterizedType) {
                    bound = ((ParameterizedType) bound).getRawType();
                    if (bound instanceof Class && Enum.class.isAssignableFrom((Class<?>) bound)) {
                        return String.class;
                    }
                }
            }
        }
        return this.type;
    }

    private String getColumnDefinition(final Dialect dialect) {
        if (columnDefinition != null) {
            return columnDefinition;
        }
        StringBuilder definition = new StringBuilder();
        definition.append(typeName);
        if (!nullable) {
            definition.append(" not null");
        }
        if (defaultValue != null) {
            definition.append(" default ").append(defaultValue);
        }
        if (!dialect.isSupportCommentOn() && StringUtils.nonEmpty(comment)) {
            definition.append(" comment '").append(comment).append("'");
        }
        return definition.toString();
    }

    private static String getPrimaryKeyDefinition(
            final Dialect dialect, final EntityColumn entityColumn, final IdGenerator idGenerator) {
        final Type type = entityColumn.type;
        // 只有主键类型是整型时,才可能自增
        if (!(type instanceof Class)
                || !(long.class.equals(type)
                || Long.class.equals(type)
                || int.class.equals(type)
                || Integer.class.equals(type)
                || short.class.equals(type)
                || Short.class.equals(type))) {
            return entityColumn.columnDefinition;
        }
        Object id = 1L;
        try {
            id = idGenerator.nextId(null);
        } catch (Exception ignore) {
            // 用户自定义的id生成器可能会用到obj参数,此时会抛异常,认为主键非自增
        }
        // 只有id为null时才拼接自增定义
        final IdentityColumnSupport identityColumnSupport = dialect.getIdentityColumnSupport();
        return id == null
                ? identityColumnSupport.containDataTypeInIdentityColumn()
                ? identityColumnSupport.getIdentityColumnString(type)
                : dialect
                .getTypeName(type)
                .concat(" ")
                .concat(identityColumnSupport.getIdentityColumnString(type))
                : entityColumn.columnDefinition;
    }

    public boolean nonIgnore() {
        return !ignore;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final EntityColumn column = (EntityColumn) o;
        return Objects.equals(name, column.name) && Objects.equals(type, column.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
