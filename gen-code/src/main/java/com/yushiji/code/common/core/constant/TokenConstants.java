package com.yushiji.code.common.core.constant;

/**
 * Token的Key常量
 *
 * @author ruoyi
 */
public class TokenConstants {
    /**
     * 令牌自定义标识
     */
    public static final String AUTHENTICATION = "Authorization";
    /**
     * websocket子协议，这里用来鉴定权限
     */
    public static final String WEBSOCKET_AUTHENTICATION = "sec-websocket-protocol";

    /**
     * 登出时额外携带token
     */
    public static final String REFRESH_AUTHENTICATION = "Authorization";
    /**
     * 令牌前缀
     */
    public static final String PREFIX = "Bearer ";

    /**
     * 令牌秘钥
     */
    public final static String SECRET = "cierpproject46802971";

}
