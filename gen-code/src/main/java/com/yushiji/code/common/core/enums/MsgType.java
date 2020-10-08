package com.yushiji.code.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MsgType {

    HEART(0, "heart"),
    CHAT(1, "chat"),
    AUDIT(2, "audit"),
    HINT(3, "hint");

    private final int code;
    private final String type;

    /**
     * 什么都不传就走心跳的处理(心跳默认无处理)
     *
     * @return type
     */
    public static MsgType getTypeByCode() {
        return getTypeByCode(0);
    }

    public static MsgType getTypeByCode(int code) {
        MsgType[] values = MsgType.values();
        for (MsgType value : values) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return MsgType.HEART;
    }
}
