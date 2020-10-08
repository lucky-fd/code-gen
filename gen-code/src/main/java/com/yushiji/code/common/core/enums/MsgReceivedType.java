package com.yushiji.code.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MsgReceivedType {

    unread("0", "未接收"),
    unconfirmed("1", "未确认"),
    confirmed("2", "已确认");

    private final String code;
    private final String type;
}
