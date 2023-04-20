package com.yushi.code.common.core.exception;

public class TokenRefreshException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TokenRefreshException(String message) {
        super(message);
    }

    public TokenRefreshException(Throwable cause) {
        super(cause);
    }

    public TokenRefreshException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenRefreshException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
