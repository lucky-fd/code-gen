package com.yushiji.code.common.core.constant;

import org.springframework.stereotype.Component;

/*
    供应商常量池
 */
@Component
public class SupplierConstants {
    /**
     * 收货状态：待收货
     */
    public final static String PURCHASE_RECEIVE_WAIT = "0";

    /**
     * 收货状态：已收货
     */
    public final static String PURCHASE_RECEIVE_STATUS_RECEIVED = "1";

    /**
     * 收货状态：待检测
     */
    public final static String PURCHASE_RECEIVE_STATUS_QUALITY = "2";

    /**
     * 收货状态：检测中
     */
    public final static String PURCHASE_RECEIVE_QUALITYED= "3";


}
