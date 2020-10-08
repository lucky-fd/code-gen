package com.yushiji.code.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonExportType {

    USER("user", "用户"),

    DEPT("dept", "部门");


    private final String code;
    private final String value;

    public static String getValueByCode(String code) {
        for (CommonExportType type : values()) {
            if (type.getCode().equals(code)) {
                return type.getValue();
            }
        }
        return null;
    }
}
