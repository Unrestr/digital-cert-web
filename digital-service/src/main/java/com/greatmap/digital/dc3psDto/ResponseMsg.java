package com.greatmap.digital.dc3psDto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.greatmap.digital.enums.TxStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 响应消息实体,用于与第三方对接时封装响应
 * @author：sms
 */
public class ResponseMsg implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseMsg.class);

    /**
     * 响应方的响应日期(格式:20180315)
     */
    private String receiveDate;
    /**
     * 响应方的响应时间格式:163512)
     */
    private String receiveTime;
    /**
     * 响应方的id
     */
    private String receiverId;
    /**
     * 响应方返回的响应消息字段
     */
    private String rtnMessage;
    /**
     * 响应方返回的状态字段(0:成功,1:失败)
     */
    private String txStatus;
    /**
     * 响应方返回报文的序列号
     */
    private Long receiveSeqNo;
    /**
     *当前返回记录数(分页查询用必填)
     */
    private Integer recordNum;
    /**
     *总记录数(分页查询用必填)
     */
    private Integer totalNum;

    /**
     * 响应数据data字段
     */
    private Object data;


    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getRtnMessage() {
        return rtnMessage;
    }

    public void setRtnMessage(String rtnMessage) {
        this.rtnMessage = rtnMessage;
    }

    public String getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(String txStatus) {
        this.txStatus = txStatus;
    }

    public Long getReceiveSeqNo() {
        return receiveSeqNo;
    }

    public void setReceiveSeqNo(Long receiveSeqNo) {
        this.receiveSeqNo = receiveSeqNo;
    }

    public Integer getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(Integer recordNum) {
        this.recordNum = recordNum;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @JSONField(serialize=false)
    public TxStatusEnum getTxStatusEnum(){
        return TxStatusEnum.getTxStatusEnum(this.txStatus);
    }

    public void setTxStatusEnum(TxStatusEnum txStatusEnum){
        this.txStatus = txStatusEnum.getCode();
    }

    public ResponseMsg() {
    }

    public ResponseMsg(TxStatusEnum txStatusEnum, String rtnMessage) {
        this.txStatus = txStatusEnum.getCode();
        this.rtnMessage = rtnMessage;
    }

    @Override
    public String toString()
    {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

    public String toMohurdString(){
        JSONObject jo = new JSONObject();
        jo.put("ReceiveDate", receiveDate);
        jo.put("ReceiveTime", receiveTime);
        jo.put("ReceiverId", receiverId);
        jo.put("ReceiveNode", "C65000");
        jo.put("RtnMessage", rtnMessage);
        jo.put("ReceiveSeqNo", receiveSeqNo);
        jo.put("RecoNum", recordNum);
        jo.put("TotNum", totalNum);
        jo.put("Data", data);
        if(getTxStatusEnum() == TxStatusEnum.RESPONSE_FAIL){
            jo.put("TxStatus", TxStatusEnum.RESPONSE_SUCCESS.getCode());
        }else if(getTxStatusEnum() == TxStatusEnum.RESPONSE_SUCCESS){
            jo.put("TxStatus", TxStatusEnum.RESPONSE_FAIL.getCode());
        }
        return JSON.toJSONString(jo, SerializerFeature.WriteMapNullValue);
    }

    public String toTaxString(){
        JSONObject jo = new JSONObject();
        jo.put("receiveDate", receiveDate);
        jo.put("receiveTime", receiveTime);
        jo.put("receiverId", receiverId);
        jo.put("rtnMessage", rtnMessage);
        jo.put("receiveSeqNo", receiveSeqNo);
        jo.put("recordNum", recordNum);
        jo.put("totalNum", totalNum);
        jo.put("data", data);
        if(getTxStatusEnum() == TxStatusEnum.RESPONSE_FAIL){
            jo.put("txStatus", TxStatusEnum.RESPONSE_SUCCESS.getCode());
        }else if(getTxStatusEnum() == TxStatusEnum.RESPONSE_SUCCESS){
            jo.put("txStatus", TxStatusEnum.RESPONSE_FAIL.getCode());
        }
        return JSON.toJSONString(jo, SerializerFeature.WriteMapNullValue);
    }

    public static ResponseMsg getFromMohurd(String jsonStr){
        JSONObject jo = JSONObject.parseObject(jsonStr);
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setReceiveDate(jo.getString("ReceiveDate"));
        responseMsg.setReceiveTime(jo.getString("ReceiveTime"));
        responseMsg.setReceiverId(jo.getString("ReceiverId"));
        responseMsg.setRtnMessage(jo.getString("RtnMessage"));
        responseMsg.setReceiveSeqNo(jo.getLong("ReceiveSeqNo"));
        responseMsg.setRecordNum(jo.getInteger("RecoNum"));
        responseMsg.setTotalNum(jo.getInteger("TotNum"));
        responseMsg.setData(jo.get("Data"));
        String txStatus = jo.getString("TxStatus");
        if(TxStatusEnum.RESPONSE_FAIL.getCode().equals(txStatus)){
            responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_SUCCESS);
        }else if(TxStatusEnum.RESPONSE_SUCCESS.getCode().equals(txStatus)){
            responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_FAIL);
        }
        return responseMsg;
    }

    public static ResponseMsg getFromTax(String jsonStr){
        ResponseMsg responseMsg = JSON.parseObject(jsonStr, ResponseMsg.class);
        TxStatusEnum txStatusEnum = responseMsg.getTxStatusEnum();
        if(txStatusEnum == TxStatusEnum.RESPONSE_FAIL){
            responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_SUCCESS);
        }else if(txStatusEnum == TxStatusEnum.RESPONSE_SUCCESS){
            responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_FAIL);
        }
        return responseMsg;
    }
}
