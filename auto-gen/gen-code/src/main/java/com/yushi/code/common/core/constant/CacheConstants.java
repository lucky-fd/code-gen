package com.yushi.code.common.core.constant;

import org.springframework.stereotype.Component;

/**
 * 缓存的key 常量
 *
 * @author fdong
 */
@Component
public class CacheConstants {


    /**
     * 自定义缓存时间占位符
     */
    public final static String SPRING_CACHE_TTL = "#";

    /**
     * spring缓存key后缀
     */
    public final static String SPRING_SUFFIX = "::";

    /**
     * 正常分隔
     */
    public final static String SUFFIX = ":";

    /**
     * redis查询
     */
    public final static String KEY_MATCH = "*";
    /**
     * 缓存有效期，默认720（分钟）
     */
    public final static long EXPIRATION = 720;

    /**
     * 缓存刷新时间，默认120（分钟）
     */
    public final static long REFRESH_TIME = 120;

    /**
     * 权限缓存前缀
     */
    public final static String LOGIN_TOKEN_KEY = "sys_user:login_tokens:";


    /**
     * 消息未读过期时间，默认7（天）
     */
    public final static long MSG_UNREAD_EXPIRATION = 7;

    /**
     * mq判重过期时间，默认1（天）
     */
    public final static long REPETITION_MQ_FLAG_EXPIRATION = 1;


    /**
     * 注册消息缓存前缀
     */
    public final static String MSG_UNREAD_KEY = "msg_message:unread:";

    /**
     * mq防重复flag
     */
    public static final String REPETITION_MQ_FLAG = "common_mq:repetition:";
    /**
     * 流程图片key
     */
    public static final String PROCESS_IMG = "sys_flowable:process_img";
    /**
     * 流程图片过期时间
     */
    public static final long PROCESS_IMG_EXPIRATION = 7 * 60 * 60;
    /**
     * 流程过期时间ttl
     */
    public final static String PROCESS_IMG_KEY_TTL = PROCESS_IMG + SPRING_CACHE_TTL + PROCESS_IMG_EXPIRATION;

    /**
     * 流水号缓存key前缀
     */
    public static final String SERIAL_CACHE_PREFIX = "common_serialnumber:generate:";

    /**
     * 默认缓存天数
     */
    public static final Integer DEFAULT_CACHE_DAYS = 3;

    /**
     * 字典选择缓存
     */
    public static final String SYS_DICT = "sys_dict:";

    //前缀
    public static final String SELECT_PREFIX = "common_select";

    /**
     * 系统服务用户选择框缓存
     */
    public static final String SYSTEM_USER = "sys_user";
    public static final String SYSTEM_USER_SELECT = SELECT_PREFIX + SUFFIX + SYSTEM_USER;//用户查询框

    /**
     * 系统服务用户部门缓存
     */
    public static final String SYSTEM_DEPT = "sys_dept";
    public static final String SYSTEM_DEPT_SELECT_SPRING = SELECT_PREFIX + SPRING_SUFFIX + SYSTEM_DEPT; //部门查询框

    public static final String SYSTEM_ROLE = "sys_role";

    /**
     * 日常服务选择框产品缓存
     */
    public static final String DAILY_PRODUCT = "daily_product";

    public static final String DAILY_PRODUCT_SELECT = SELECT_PREFIX + SUFFIX + DAILY_PRODUCT;

    public static final String DAILY_PRODUCT_TREE_SELECT = "common_select_tree:daily_product";


    /**
     * 日常服务仓库缓存
     */
    public static final String DAILY_WAREHOUSE = "daily_wareHouse";

    public static final String DAILY_WAREHOUSE_SELECT_SPRING = SELECT_PREFIX + SPRING_SUFFIX + DAILY_WAREHOUSE;

    //时间类型解析
    public final static String TIME = "DATE";


    /**
     * 文件系统下载key前缀
     */
    public final static String FILE_DOWN = "file_down:";
    //私有文件
    public final static String FILE_DOWN_PRI = FILE_DOWN + "pri_bucket:";
    //公共文件
    public final static String FILE_DOWN_PUB = FILE_DOWN + "pub_bucket:";

    /**
     * table缓存
     *
     */
    public final static String TABLE_CACHE = "table_cache";

    //部门选择缓存
    public final static String TABLE_DEPT_CACHE = TABLE_CACHE + SUFFIX + SYSTEM_DEPT;

    //用户缓存
    public final static String TABLE_USER_CACHE = TABLE_CACHE + SUFFIX + SYSTEM_USER;

    //权限缓存
    public final static String TABLE_ROLE_CACHE = TABLE_CACHE + SUFFIX + SYSTEM_ROLE;

    //产品缓存
    public final static String TABLE_PRODUCT_CACHE = TABLE_CACHE + SUFFIX + DAILY_PRODUCT;

    //库存缓存
    public final static String TABLE_WAREHOUSE_CACHE = TABLE_CACHE + SUFFIX + DAILY_WAREHOUSE;

    public final static String TABLE_ROLE_USER_CACHE = TABLE_CACHE + "role_user_cache";


    /**
     * 接口时间限制
     */
    public final static String TIME_LIMIT_CACHE = "interface_limit";

    /**
     * 流程审批key
     */
    public final static String PROCESS_COMPLETE_PREFIX = "process_complete_cache:";
    /**
     * 流程拒绝key
     */
    public final static String PROCESS_REJECT_PREFIX = "process_reject_cache:";
    /**
     * 服务更新key
     */
    public static final String SERVICE_UPDATE = "sys_service_update";
}
