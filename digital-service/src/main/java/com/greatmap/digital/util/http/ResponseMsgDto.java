package com.greatmap.digital.util.http;

import com.alibaba.fastjson.JSON;
import com.greatmap.digital.excepition.DigitalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.URLEncoder;

/**
 * 响应实体
 * @author gaorui
 */
public class ResponseMsgDto implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseMsgDto.class);

    /**
     * 响应码
     */
    private String code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private Object data;

    public ResponseMsgDto(ResponseCodeEnum responseCodeEnum, String msg, Object data)
    {
        this.code = responseCodeEnum.getCode();
        this.msg = msg;
        this.data = data;
    }
    public ResponseMsgDto(ResponseCodeEnum responseCodeEnum, String msg)
    {
        this.code = responseCodeEnum.getCode();
        this.msg = msg;
    }
    public ResponseMsgDto(ResponseCodeEnum responseCodeEnum)
    {
        this.code = responseCodeEnum.getCode();
        this.msg = responseCodeEnum.getDesc();
    }
    public ResponseMsgDto(ResponseCodeEnum responseCodeEnum, Object data)
    {
        this.code = responseCodeEnum.getCode();
        this.msg = responseCodeEnum.getDesc();
        this.data = data;
    }
    public ResponseMsgDto(Object data)
    {
        this.code = ResponseCodeEnum.SUCCESS.getCode();
        this.msg = ResponseCodeEnum.SUCCESS.getDesc();
        this.data = data;
    }
    public ResponseMsgDto()
    {
        this.code = ResponseCodeEnum.SUCCESS.getCode();
        this.msg = ResponseCodeEnum.SUCCESS.getDesc();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    @Override
    public String toString()
    {
        return JSON.toJSONString(this);
    }

    public String toURLEncodeString()
    {
        String data = JSON.toJSONString(this);
        try {
            data = URLEncoder.encode(data, "UTF-8");
        } catch (Exception e) {
            LOGGER.error("对响应报文URL编码时出现异常！报文内容:" + data, e);
            throw new DigitalException("对响应报文URL编码时出现异常！报文内容:" + data, e);
        }
        return data;
    }
}
