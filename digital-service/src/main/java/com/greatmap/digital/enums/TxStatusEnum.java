package com.greatmap.digital.enums;

import com.greatmap.framework.commons.utils.StringUtils;


/**
 * @author chen
 */
public enum TxStatusEnum {

    /**
     * 响应失败
     */
    RESPONSE_FAIL("0","响应失败"),

    /**
     * 响应成功
     */
    RESPONSE_SUCCESS("1","响应成功");

    private String code;
    private String desc;

    TxStatusEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }

    public static TxStatusEnum getTxStatusEnum(String txStatus){
        if(StringUtils.isNotBlank(txStatus)){
            TxStatusEnum[] enums =  TxStatusEnum.values();
            for (TxStatusEnum item :enums){
                if(txStatus.equals(item.getCode())){
                    return item;
                }
            }
        }
        return null;
    }

}
