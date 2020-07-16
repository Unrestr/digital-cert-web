package com.greatmap.digital.excepition;

/**
 * 数据服务平台封装异常类
 * @author greatmap
 */
public class DigitalThirdException extends RuntimeException{

    public DigitalThirdException() {
        super();
    }

    public DigitalThirdException(String message) {
        super(message);
    }

    public DigitalThirdException(String message, Throwable cause) {
        super(message, cause);
    }

    public DigitalThirdException(Throwable cause) {
        super(cause);
    }

    protected DigitalThirdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
