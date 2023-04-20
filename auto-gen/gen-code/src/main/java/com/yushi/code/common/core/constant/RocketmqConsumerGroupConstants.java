package com.yushi.code.common.core.constant;

public class RocketmqConsumerGroupConstants {

    /**
     * 流程信息提示的group
     */
    public static final String MSG_CONSUMER_HINT_GROUP = "MSG_CONSUMER";

    /**
     * 流程审核消息的group
     */
    public static final String MSG_CONSUMER_AUDIT_GROUP = "MSG_AUDIT_CONSUMER";
    /**
     * 存储登录信息的group
     */
    public static final String AUTH_LOGIN_INFO_CONSUMER_GROUP = "LOGIN_CONSUMER";

    /**
     * 注册消息审核信息的group
     */
    public static final String REGISTER_CONSUMER_GROUP = "REGISTER_CONSUMER";
    /**
     * 文件信息的group
     */
    public static final String FILE_MSG_CONSUMER_GROUP = "FILE_MSG_CONSUMER";

    /**
     * 流程信息的group
     */
    public static final String PROCESS_COMPLETE_CONSUMER_GROUP = "PROCESS_COMPLETE_CONSUMER";
    /**
     * canal缓存group
     */
    public static final String SYS_USER_CACHE_GROUP = "SYS_USER_CACHE_GROUP";
    public static final String SYS_PRODUCT_CACHE_GROUP = "SYS_PRODUCT_CACHE_GROUP";
}
