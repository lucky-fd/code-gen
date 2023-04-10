package com.yushiji.code.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhangwenhao
 * 返回信息枚举类
 */
@Getter
@AllArgsConstructor
public enum ResCode {
    ACCESS_TOKEN_EXPIRE(10008, "授权已过期");

    private final Integer code;
    private final String msg;


}
