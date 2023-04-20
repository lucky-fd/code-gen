package com.yushi.code.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
    通用审批方式
    字典：common_audit_method
 */
@Getter
@AllArgsConstructor
public enum EmCommonAudit {
    AGREE("1", "拒绝(不满足)"),
    REJECT("2", "通过(满足)");

    private final String code;
    private final String value;

    public static String getValueByKey(String key) {
        for (EmCommonAudit type : values()) {
            if (type.getCode().equals(key)) {
                return type.getValue();
            }
        }
        return null;
    }
}
