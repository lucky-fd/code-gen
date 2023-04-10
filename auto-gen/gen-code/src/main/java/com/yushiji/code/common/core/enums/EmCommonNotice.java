package com.yushiji.code.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
       通用通知状态
       字典：common_notice_status
 */
@Getter
@AllArgsConstructor
public enum EmCommonNotice {
    NOTICE_ING("1", "待通知"),
    NOTICE_ED("2", "已通知");

    private final String code;
    private final String value;

    public static String getValueByKey(String key) {
        for (EmCommonNotice type : values()) {
            if (type.getCode().equals(key)) {
                return type.getValue();
            }
        }
        return null;
    }
}
