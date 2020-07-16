package com.greatmap.digital.util.http;


/**
 * 响应码枚举类
 *
 * @author Created by gaorui
 */
public enum ResponseCodeEnum {
    /**
     * 操作成功
     */
    SUCCESS("200", "操作成功"),
    /**
     * 响应错误
     */
    ERROR("001", "响应错误"),
    /**
     * 参数错误
     */
    ARGUMENTS_ERROR("002", "参数错误"),
    /**
     * 参数为空
     */
    ARGUMENTS_EMPTY("003", "参数为空"),
    /**
     * 网络超时
     */
    NET_TIMEOUT("004", "网络超时"),
    /**
     * 解密错误
     */
    DECRYPT_ERROR("005", "解密错误"),
    /**
     * http请求头未带systemCode信息
     */
    SYSTEM_CODE_NOT_EXIST_ON_HEADER("006", "http请求头未带systemCode信息"),
    /**
     * 系统公钥未配置
     */
    SYSTEM_PUBLIC_KEY_NOT_SET("007", "系统公钥未配置"),
    /**
     * 响应消息格式错误
     */
    INVALID_RESPONSE_MSG_FORMAT("008", "响应消息格式错误"),
    /**
     * 加密响应消息时发生异常
     */
    ENCRYPT_RESPONSE_MSG_ERROR("009", "加密响应消息时发生异常"),
    /**
     *用户或服务未授权,请向数据共享平台申请
     */
    DSP_UNAUTHORIZED("403","用户或服务未授权,请向数据共享平台申请!"),
    /**
     *等待数据共享平台响应(数据共享平台请求处理中)
     */
    DSP_DEALING("404","数据共享平台请求处理中!"),
    /**
     * 其他错误
     */
    OTHER_ERROR("999", "其他错误"),

    /**
     *校验签名失败
     */
    ANALYSIS_ERROR("101","校验签名错误!");



    private String code;
    private String desc;

    ResponseCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }

    public static String getResponseDesc(String code) {
        for (ResponseCodeEnum responseCodeEnum : ResponseCodeEnum.values()) {
            if (code.equals(responseCodeEnum.getCode())) {
                return responseCodeEnum.getDesc();
            }
        }
        return null;
    }

    public static ResponseCodeEnum getResponseCodeEnum(String code) {
        for (ResponseCodeEnum responseCodeEnum : ResponseCodeEnum.values()) {
            if (code.equals(responseCodeEnum.getCode())) {
                return responseCodeEnum;
            }
        }
        return null;
    }
}
