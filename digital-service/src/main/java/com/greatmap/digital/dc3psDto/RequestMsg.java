package com.greatmap.digital.dc3psDto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 与第三方交互的请求消息实体
 * @author：zxc
 * @date：2019/11/15
 */
public class RequestMsg implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestMsg.class);

    /**
     * 请求日期(格式:20180315)
     */
    private String sendDate;
    /**
     * 请求时间(格式:163512)
     */
    private String sendTime;
    /**
     * 请求消息序列号
     */
    private Long sendSeqNo;
    /**
     * 操作员编号
     */
    private String operNo;
    /**
     *查询页码
     */
    private Integer pageNum;
    /**
     *每页记录数
     */
    private Integer pageSize;

    /**
     * 请求报文的data字段
     */
    private Object data;

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Long getSendSeqNo() {
        return sendSeqNo;
    }

    public void setSendSeqNo(Long sendSeqNo) {
        this.sendSeqNo = sendSeqNo;
    }

    public String getOperNo() {
        return operNo;
    }

    public void setOperNo(String operNo) {
        this.operNo = operNo;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

    public String toMohurdString(){
        JSONObject jo = new JSONObject();
        jo.put("SendDate", sendDate);
        jo.put("SendTime", sendTime);
        jo.put("SendSeqNo", sendSeqNo);
        jo.put("SendNode", "C65000");
        jo.put("SendSeqNo", sendSeqNo);
        jo.put("OperNo", operNo);
        jo.put("PageNum", pageNum);
        jo.put("PageSize", pageSize);
        jo.put("Data", data);
        return JSON.toJSONString(jo, SerializerFeature.WriteMapNullValue);
    }
}
