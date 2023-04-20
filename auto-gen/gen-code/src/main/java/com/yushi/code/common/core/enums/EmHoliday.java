package com.yushi.code.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmHoliday {

    HOLIDAY("1", "节假日"),
    WORK_TAKE("2", "调休"),
    SUNDAY("3", "周天"),
    WORK_DAY("4", "工作日");

    private final String code;
    private final String value;

    public static String getValueByKey(String key) {
        for (EmHoliday type : values()) {
            if (type.getCode().equals(key)) {
                return type.getValue();
            }
        }
        return null;
    }
}
