package com.yushi.code.common.core.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MsgResult {
    /**
     * 发送成功
     */
    SEND_SUCCESS(1, "发送成功"),
    /**
     * 发送用户未指定，进行群发
     */
    PERSON_NULL_GROUP_SEND(3, "发送用户未指定，进行群发"),
    /**
     * "消息为空，不进行发送"
     */
    MSG_NULL_NOT_SEND(4, "消息为空，不进行发送"),
    /**
     * 发送的消息为空,不进行发送
     */

    SEND_NULL(5, "发送的消息为空,不进行发送");

    /**
     * 编码
     */
    private final int code;
    /**
     * 内容
     */
    private final String title;

    /**
     * 获取编码
     *
     * @return int
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取标题
     *
     * @return String
     */
    public String getTitle() {
        return title;
    }
}
