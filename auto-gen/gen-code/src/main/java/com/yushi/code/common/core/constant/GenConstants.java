package com.yushi.code.common.core.constant;

/**
 * 代码生成通用常量
 *
 * @author fdong
 * @since 2020.6.8
 */
public class GenConstants {
    /**
     * 单表（增删改查）
     */
    public static final String TPL_CRUD = "crud";

    /**
     * 树表（增删改查）
     */
    public static final String TPL_TREE = "tree";

    /**
     * 主子表（增删改查）
     */
    public static final String TPL_SUB = "sub";
    /**
     * 基础增删改查
     */
    public static final String BASE_TYPE = "base";
    /**
     * plus版
     */
    public static final String PLUS_TYPE = "plus";

    /**
     * 自定义
     * */
    public static final String CUSTOM_TYPE = "custom";

    /**
     * 树编码字段
     */
    public static final String TREE_CODE = "treeCode";

    /**
     * 树父编码字段
     */
    public static final String TREE_PARENT_CODE = "treeParentCode";

    /**
     * 树名称字段
     */
    public static final String TREE_NAME = "treeName";

    /**
     * 上级菜单ID字段
     */
    public static final String PARENT_MENU_ID = "parentMenuId";

    /**
     * 上级菜单名称字段
     */
    public static final String PARENT_MENU_NAME = "parentMenuName";

    /**
     * 数据库字符串类型
     */
    public static final String[] COLUMNTYPE_STR = {"char", "varchar", "nvarchar", "varchar2"};

    /**
     * 数据库文本类型
     */
    public static final String[] COLUMNTYPE_TEXT = {"tinytext", "text", "mediumtext", "longtext"};

    /**
     * 数据库时间类型
     */
    public static final String[] COLUMNTYPE_TIME = {"datetime", "time", "date", "timestamp"};

    /**
     * 数据库数字类型
     */
    public static final String[] COLUMNTYPE_NUMBER = {"smallint", "mediumint", "int", "number", "integer",
            "bigint", "float", "double", "decimal"};

    public static final String[] COLUMNTYPE_TINY_NUMBER = {"tinyint"};

    /**
     * 页面不需要编辑字段
     */
    public static final String[] COLUMNNAME_NOT_EDIT = {"id", "create_by", "create_time", "del_flag"};
//    ,"re_mark2","re_mark3","re_mark4","re_mark5","re_mark6","re_mark7","re_mark8","re_mark9","re_mark10","re_mark11","re_mark12","re_mark13","re_mark14","re_mark15","re_mark16","re_mark17","re_mark18","re_mark19"

    /**
     * 页面不需要显示的列表字段
     */
    public static final String[] COLUMNNAME_NOT_LIST = {"id", "create_by", "create_time", "del_flag", "update_by",
            "update_time"};
//"re_mark2","re_mark3","re_mark4","re_mark5","re_mark6","re_mark7","re_mark8","re_mark9","re_mark10","re_mark11","re_mark12","re_mark13","re_mark14","re_mark15","re_mark16","re_mark17","re_mark18","re_mark19"
    /**
     * 页面不需要查询字段
     */
    public static final String[] COLUMNNAME_NOT_QUERY = {"id", "create_by", "create_time", "del_flag", "update_by",
            "update_time", "remark","re_mark2","re_mark3","re_mark4","re_mark5","re_mark6","re_mark7","re_mark8","re_mark9","re_mark10","re_mark11","re_mark12","re_mark13","re_mark14","re_mark15","re_mark16","re_mark17","re_mark18","re_mark19"};

    /**
     * Entity基类字段
     */
    public static final String[] BASE_ENTITY = {"id", "createTime", "updateTime", "remark","active","delFlag"};

    /**
     * Tree基类字段
     */
    public static final String[] TREE_ENTITY = {"parentName", "parentId", "orderNum", "ancestors"};

    /**
     * 文本框
     */
    public static final String HTML_INPUT = "input";

    /**
     * 文本域
     */
    public static final String HTML_TEXTAREA = "textarea";

    /**
     * 下拉框
     */
    public static final String HTML_SELECT = "select";

    /**
     * 单选框
     */
    public static final String HTML_RADIO = "radio";

    /**
     * 复选框
     */
    public static final String HTML_CHECKBOX = "checkbox";

    /**
     * 日期控件
     */
    public static final String HTML_DATETIME = "datetime";

    /**
     * 图片上传控件
     */
    public static final String HTML_IMAGE_UPLOAD = "imageUpload";

    /**
     * 文件上传控件
     */
    public static final String HTML_FILE_UPLOAD = "fileUpload";

    /**
     * 富文本控件
     */
    public static final String HTML_EDITOR = "editor";

    /**
     * 字符串类型
     */
    public static final String TYPE_STRING = "String";

    /**
     * 整型
     */
    public static final String TYPE_INTEGER = "Integer";

    /**
     * 长整型
     */
    public static final String TYPE_LONG = "Long";

    /**
     * 浮点型
     */
    public static final String TYPE_DOUBLE = "Double";

    /**
     * 高精度计算类型
     */
    public static final String TYPE_BIGDECIMAL = "BigDecimal";

    /**
     * 时间类型
     */
    public static final String TYPE_DATE = "Date";

    /**
     * 时间类型
     */
    public static final String TYPE_BOOLEAN = "Boolean";


    public static final String TYPE_DATE_TIME = "LocalDateTime";

    /**
     * 模糊查询
     */
    public static final String QUERY_LIKE = "LIKE";

    /**
     * 相等查询
     */
    public static final String QUERY_EQ = "EQ";

    /**
     * 需要
     */
    public static final String REQUIRE = "1";
}
