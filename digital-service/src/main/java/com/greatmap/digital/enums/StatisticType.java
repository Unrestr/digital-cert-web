package com.greatmap.digital.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author greatmap
 */
public enum StatisticType {

    /**
     * 当前周
     */
    DAY("0","周"),

    /**
     * 当前周
     */
    WEEK("1","周"),

    /**
     * 当前月
     */
    MONTH("2","月"),

    /**
     * 当前年
     */
    YEAR("3","年");


    private String code;
    private String description;

    StatisticType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static StatisticType getByCode(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        StatisticType[] djdlEnas =  StatisticType.values();
        for (StatisticType item :djdlEnas){
            if(code.equals(item.getCode())){
                return item;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
