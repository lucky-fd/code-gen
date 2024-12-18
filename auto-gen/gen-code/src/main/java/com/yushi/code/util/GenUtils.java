package com.yushi.code.util;

import com.yushi.code.common.core.constant.GenConstants;
import com.yushi.code.common.core.utils.SpringUtils;
import com.yushi.code.common.core.utils.StringUtils;
import com.yushi.code.config.GenConfig;
import com.yushi.code.domain.GenTable;
import com.yushi.code.domain.GenTableColumn;
import org.apache.commons.lang3.RegExUtils;

import java.util.Arrays;

import static com.yushi.code.common.core.constant.GenConstants.*;

/**
 * 代码生成器 工具类
 *
 * @author lucky_fd
 * @since 2020.6.8
 */
public class GenUtils {
    /**
     * 初始化表信息
     */
    public static void initTable(GenTable genTable, String operName) {
        genTable.setClassName(convertClassName(genTable.getTableName()));
        genTable.setPackageName("com.ci.erp.system");
        genTable.setModuleName("system");
        genTable.setBusinessName(getBusinessName(genTable.getTableName()));
        genTable.setFunctionName("XXX菜单");
        genTable.setFunctionAuthor("XXX");
        genTable.setCreateBy(operName);
    }

    /**
     * 初始化列属性字段
     */
    public static void initColumnField(GenTableColumn column, GenTable table) {
        // 数据库类型
        String dataType = getDbType(column.getColumnType());
        String columnName = column.getColumnName();
        column.setTableId(table.getTableId());
        column.setCreateBy(table.getCreateBy());
        // 设置java字段名
        column.setJavaField(StringUtils.toCamelCase(columnName));
        // 获取Java类型
        final String javaType = getJavaType(column.getDataType(), column.getColumnType());
        // 设置类型
        column.setJavaType(javaType);
        column.setQueryType(GenConstants.QUERY_EQ);

        if (arraysContains(GenConstants.COLUMNTYPE_STR, dataType) || arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType)) {
            // 字符串长度超过500设置为文本域
            Integer columnLength = getColumnLength(column.getColumnType());
            String htmlType = columnLength >= 500 || arraysContains(GenConstants.COLUMNTYPE_TEXT, dataType) ? GenConstants.HTML_TEXTAREA : GenConstants.HTML_INPUT;
            column.setHtmlType(htmlType);
        } else if (arraysContains(GenConstants.COLUMNTYPE_TIME, dataType)) {
            // column.setJavaType(GenConstants.TYPE_DATE);
            column.setHtmlType(GenConstants.HTML_DATETIME);
        } else if (arraysContains(GenConstants.COLUMNTYPE_TINY_NUMBER, dataType)) {
            // column.setJavaType(GenConstants.TYPE_BOOLEAN);
            column.setHtmlType(GenConstants.HTML_INPUT);
        } else if (arraysContains(GenConstants.COLUMNTYPE_NUMBER, dataType)) {
            column.setHtmlType(GenConstants.HTML_INPUT);

            // 如果是浮点型 统一用BigDecimal
            // String[] str = StringUtils.split(StringUtils.substringBetween(column.getColumnType(), "(", ")"), ",");
            // if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0) {
            //     column.setJavaType(TYPE_BIGDECIMAL);
            // }
            // // 如果是整形
            // else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 10) {
            //     column.setJavaType(GenConstants.TYPE_INTEGER);
            // }
            // // 长整形
            // else {
            //     column.setJavaType(GenConstants.TYPE_STRING);
            // }
        }

        // 插入字段（默认所有字段都需要插入）
        column.setIsInsert(GenConstants.REQUIRE);

        // 编辑字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_EDIT, columnName) && !column.isPk()) {
            column.setIsEdit(GenConstants.REQUIRE);
        }
        // 列表字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_LIST, columnName) && !column.isPk()) {
            column.setIsList(GenConstants.REQUIRE);
        }
        // 查询字段
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_QUERY, columnName) && !column.isPk()) {
            column.setIsQuery(GenConstants.REQUIRE);
        }

        // 查询字段类型
        if (StringUtils.endsWithIgnoreCase(columnName, "name")) {
            column.setQueryType(GenConstants.QUERY_LIKE);
        }
        // 状态字段设置单选框
        if (StringUtils.endsWithIgnoreCase(columnName, "status")) {
            column.setHtmlType(GenConstants.HTML_RADIO);
        }
        // 类型&性别字段设置下拉框
        else if (StringUtils.endsWithIgnoreCase(columnName, "type")
                || StringUtils.endsWithIgnoreCase(columnName, "sex")) {
            column.setHtmlType(GenConstants.HTML_SELECT);
        }
        // 图片字段设置图片上传控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "image")) {
            column.setHtmlType(GenConstants.HTML_IMAGE_UPLOAD);
        }
        // 文件字段设置文件上传控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "file")) {
            column.setHtmlType(GenConstants.HTML_FILE_UPLOAD);
        }
        // 内容字段设置富文本控件
        else if (StringUtils.endsWithIgnoreCase(columnName, "content")) {
            column.setHtmlType(GenConstants.HTML_EDITOR);
        }
    }

    /**
     * 校验数组是否包含指定值
     *
     * @param arr         数组
     * @param targetValue 值
     * @return 是否包含
     */
    public static boolean arraysContains(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    /**
     * 获取模块名
     *
     * @param packageName 包名
     * @return 模块名
     */
    public static String getModuleName(String packageName) {
        int lastIndex = packageName.lastIndexOf(".");
        int nameLength = packageName.length();
        return StringUtils.substring(packageName, lastIndex + 1, nameLength);
    }

    /**
     * 获取业务名
     *
     * @param tableName 表名
     * @return 业务名
     */
    public static String getBusinessName(String tableName) {
        int lastIndex = tableName.lastIndexOf("_");
        int nameLength = tableName.length();
        return StringUtils.substring(tableName, lastIndex + 1, nameLength);
    }

    /**
     * 表名转换成Java类名
     *
     * @param tableName 表名称
     * @return 类名
     */
    public static String convertClassName(String tableName) {
        GenConfig genConfig = SpringUtils.getBean(GenConfig.class);
        boolean autoRemovePre = genConfig.isAutoRemovePre();
        String tablePrefix = genConfig.getTablePrefix();
        if (autoRemovePre && StringUtils.isNotEmpty(tablePrefix)) {
            String[] searchList = StringUtils.split(tablePrefix, ",");
            tableName = replaceFirst(tableName, searchList);
        }
        return StringUtils.convertToCamelCase(tableName);
    }

    /**
     * 批量替换前缀
     *
     * @param replacementm 替换值
     * @param searchList   替换列表
     * @return
     */
    public static String replaceFirst(String replacementm, String[] searchList) {
        String text = replacementm;
        for (String searchString : searchList) {
            if (replacementm.startsWith(searchString)) {
                text = replacementm.replaceFirst(searchString, "");
                break;
            }
        }
        return text;
    }

    /**
     * 关键字替换
     *
     * @param text 需要被替换的名字
     * @return 替换后的名字
     */
    public static String replaceText(String text) {
        return RegExUtils.replaceAll(text, "(?:表|若依)", "");
    }

    /**
     * 获取数据库类型字段
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static String getDbType(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            return StringUtils.substringBefore(columnType, "(");
        } else {
            return columnType;
        }
    }

    /**
     * 获取字段长度
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static Integer getColumnLength(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            String length = StringUtils.substringBetween(columnType, "(", ")");
            return Integer.valueOf(length);
        } else {
            return 0;
        }
    }

    /**
     * 获取数据库类型对应的java类型
     *
     * @param dataType   列数据库类型
     * @param columnType 字段定义类型
     * @return 截取后的列类型
     */
    public static String getJavaType(String dataType, String columnType) {
        switch (dataType) {
            case "bigint":
                return TYPE_LONG;
            case "binary":
                return GenConstants.TYPE_STRING;
            case "bit":
                return "byte";
            case "blob":
                return "Boolean";
            case "boolean":
                return "Boolean";
            case "bytea":
                return "byte";
            case "char":
                return "char";
            case "character":
                return "char";
            case "character varying":
                return TYPE_STRING;
            case "date":
                return "LocalDate";
            case "datetime":
                return TYPE_DATE_TIME;
            case "dicimal":
                return TYPE_BIGDECIMAL;
            case "double":
                return TYPE_DOUBLE;
            case "real": // float4
                return "Float";
            case "double precision": // float8
                return "Float";
            case "float":
                return "Float";
            case "int":
                return TYPE_INTEGER;
            case "integer":
                return TYPE_INTEGER;
            case "inet":
                return TYPE_STRING;
            case "json":
                return "Object";
            case "jsonb":
                return "Object";
            case "macaddr":
                return TYPE_STRING;
            case "numeric": // decimal
                return TYPE_BIGDECIMAL;
            case "smallint":
                return TYPE_INTEGER;
            case "text":
                return TYPE_STRING;
            case "time":
                return "LocalTime";
            case "timestamp":
                return TYPE_DATE_TIME;
            case "time without time zone":
                return TYPE_DATE_TIME;
            case "timestamp without time zone":
                return TYPE_DATE_TIME;
            case "timestamp with time zone":
                return TYPE_DATE_TIME;
            case "tinyblob":
                return TYPE_INTEGER;
            case "tinyint":
                return TYPE_INTEGER;
            case "varchar":
                return GenConstants.TYPE_STRING;
            case "varbinary":
                return GenConstants.TYPE_STRING;
            case "xml":
                return GenConstants.TYPE_STRING;
            case "ARRAY": {
                if (columnType.contains("int")) {
                    return "List<Integer>";
                } else if (columnType.contains("varchar")) {
                    return "List<String>";
                } else if (columnType.contains("text")) {
                    return "List<String>";
                } else if (columnType.contains("json")) {
                    return "List<Object>";
                } else if (columnType.contains("jsonb")) {
                    return "List<Object>";
                } else if (columnType.contains("time")) {
                    return "List<LocalTime>";
                } else if (columnType.contains("int8")) {
                    return "List<Long>";
                } else {
                    return "List";
                }
            }
            default:
                return GenConstants.TYPE_STRING;
        }

    }
}
