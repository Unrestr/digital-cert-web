package com.greatmap.digital.enums;

/**
 * @author greatmap
 */
public enum StatusEnum {

    /**
     * 现势
     */
    XS("0","现势"),

    /**
     * 注销
     */
    ZX("1","注销"),

    /**
     * 作废
     */
    ZF("2","作废");


    private String code;
    private String description;

    StatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
