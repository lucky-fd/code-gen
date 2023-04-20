package com.yushi.code.common.core.exception;

/**
 * groovy解析异常
 *
 * @author zwh
 */
public class GroovyParseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 空构造方法，避免反序列化问题
     */
    public GroovyParseException() {
    }

    public GroovyParseException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
