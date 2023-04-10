package com.yushiji.code.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuditType {

    REGISTER(0, "register");

    private final int code;
    private final String type;
}
