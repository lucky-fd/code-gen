package com.yushi.code.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MsgPassingType {
    EXTERNAL(0,"external"),
    INNER(1, "inner");

    private final int code;
    private final String type;

}
