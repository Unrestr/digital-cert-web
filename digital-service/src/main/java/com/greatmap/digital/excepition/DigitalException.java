package com.greatmap.digital.excepition;

/**
 * 数据服务平台封装异常类
 * @author greatmap
 */
public class DigitalException extends RuntimeException{

    public DigitalException() {
        super();
    }

    public DigitalException(String message) {
        super(message);
    }

    public DigitalException(String message, Throwable cause) {
        super(message, cause);
    }

    public DigitalException(Throwable cause) {
        super(cause);
    }

    protected DigitalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
