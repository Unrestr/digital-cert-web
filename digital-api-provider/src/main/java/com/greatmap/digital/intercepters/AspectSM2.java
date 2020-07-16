package com.greatmap.digital.intercepters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.greatmap.digital.dc3psDto.RequestMsg;
import com.greatmap.digital.dc3psDto.ResponseMsg;
import com.greatmap.digital.enums.TxStatusEnum;
import com.greatmap.digital.excepition.DigitalException;
import com.greatmap.digital.util.CodeGeneratorUtil;
import com.greatmap.digital.util.SM2Utils;
import com.greatmap.digital.utils.DexHttpClient;
import com.greatmap.framework.commons.utils.StringUtils;
import com.greatmap.framework.core.date.DateUtil;
import com.greatmap.framework.core.util.CollectionUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;


/**
 * 用公钥解密请求参数的、用公钥加密返回参数的切面
 *
 * @author sms
 * Created by sms on 2020/06/29
 */
@Component
@Aspect
public class AspectSM2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AspectSM2.class);

    @Value("${dex.dcPrivateKey}")
    private String dcPrivateKey;

    @Value("${dex.systemCode}")
    private String dcSystemCode;

    @Autowired
    private DexHttpClient dexHttpClient;

    @Autowired
    private CodeGeneratorUtil codeGeneratorUtil;
/*
    @Value("${moIndustryCode}")
    private String mohurdIndustryCode;
    @Value("${taxIndustryCode}")
    private String taxIndustryCode;*/


    @Pointcut("@annotation(com.greatmap.digital.intercepters.ResolveThirdParams)")
    public void doOperation() {
    }

    @Around("doOperation()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Long receiveSeqNo = codeGeneratorUtil.generateId();
        LOGGER.info("----进入第三方访问电子证照解密请求切面----请求方法:{};对应的receiveSeqNo:{}",methodName, receiveSeqNo);

        ResponseMsg responseMsg = new ResponseMsg();
        Date now = new Date();
        String receiveDate = DateUtil.format(now, "yyyyMMdd");
        String receiveTime = DateUtil.format(now, "HHmmss");
        String receiverId = dcSystemCode;
        responseMsg.setReceiveDate(receiveDate);
        responseMsg.setReceiveTime(receiveTime);
        responseMsg.setReceiverId(receiverId);
        responseMsg.setReceiveSeqNo(receiveSeqNo);

        //获取所有的头部参数
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        String allHeaderStr = "";
        for (Enumeration<String> e = headerNames; e.hasMoreElements(); ) {
            String thisName = e.nextElement();
            String thisValue = request.getHeader(thisName);
            allHeaderStr += thisName + "=" + thisValue + "&";
        }
        LOGGER.info("{}方法receiveSeqNo:{}对应的所有的请求头{}", methodName, receiveSeqNo, allHeaderStr);

        Object[] args = joinPoint.getArgs();
        String reqStr = (String) args[0];
        LOGGER.info("{}方法receiveSeqNo:{}对应的请求报文:{}", methodName, receiveSeqNo, reqStr);

        String areaCode = request.getHeader("x-area-code");
        String format = request.getHeader("x-format");
        String method = request.getHeader("x-method");
        String senderId = request.getHeader("x-sender-id");
        String timestamp = request.getHeader("x-timestamp");
        String version = request.getHeader("x-version");
        String sign = request.getHeader("x-sign");
        String xIndustryCode = request.getHeader("x-industry-code");

        //首先验证必须的请求头
        if (StringUtils.isBlank(senderId)) {
            //没有请求头x-sender-id,则取不到请求方公钥,则不能对响应加密,故直接返回明文提示消息,方便跟踪调试
            LOGGER.error("{}的receiveSeqNo:{}http请求头必须包含x-sender-id!", methodName, receiveSeqNo);
            responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_FAIL);
            responseMsg.setRtnMessage(String.format("http请求头必须包含x-sender-id!"));
            return responseMsg.toString();
        }

        //获取请求方所属行业
        String industryCode = null;
        try {
            industryCode = dexHttpClient.getSenderIndustryCode(senderId);
        } catch (Exception e) {
            LOGGER.error(String.format("receiveSeqNo:%s获取请求方%s行业编码时出现异常!", receiveSeqNo, senderId), e);
            responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_FAIL);
            responseMsg.setRtnMessage(String.format("获取请求方%s行业编码时出现异常!", senderId));
            return responseMsg.toString();
        }
        if(StringUtils.isBlank(industryCode)) {
            LOGGER.error(String.format("根据请求方%s;receiveSeqNo:%s获取所属行业编码为空,请检查共享交换配置!", senderId, receiveSeqNo));
            responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_FAIL);
            responseMsg.setRtnMessage(String.format("根据请求方%s获取所属行业编码为空,请检查共享交换配置!", senderId));
            return responseMsg.toString();
        }
        //获取加密返回报文的公钥
        String thirdPublicKey = null;
        try {
            thirdPublicKey = dexHttpClient.getSenderPublicSecret(senderId);
            if (StringUtils.isBlank(thirdPublicKey)) {
                throw new DigitalException(String.format("获取到的请求方%s公钥为空,请检查公钥配置", senderId));
            }
        } catch (Exception e) {
            LOGGER.error("receiveSeqNo:{}获取请求方{}公钥时出现异常!", receiveSeqNo, senderId, e);
            //获取不到请求方公钥,直接返回明文提示
            responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_FAIL);
            responseMsg.setRtnMessage(String.format("获取请求方%s公钥时出现异常:%s", senderId, e.getMessage()));
            /*if (mohurdIndustryCode.equals(industryCode)) {
                return responseMsg.toMohurdString();
            }
            if (taxIndustryCode.equals(industryCode)) {
                return responseMsg.toTaxString();
            } else {
                return responseMsg.toString();
            }*/
            return responseMsg.toString();
        }

        try {
            String responSign = "x-area-code" + areaCode + "x-format" + format + "x-industry-code" + xIndustryCode +
                    "x-method" + method + "x-sender-id" + senderId + "x-timestamp" + timestamp + "x-version" + version + reqStr;
            if (StringUtils.isBlank(sign)) {
                responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_FAIL);
                responseMsg.setRtnMessage(String.format("请求头x-sign不能为空！"));
            }else if(!SM2Utils.verify(thirdPublicKey, responSign, senderId, sign)){
                responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_FAIL);
                responseMsg.setRtnMessage(String.format("验签失败！"));
                LOGGER.error("receiveSeqNo:{}验签失败!根据请求拼接的待签名串:{}", receiveSeqNo, responSign);
            } else {
                String decReqStr = null;
                try {
                    decReqStr = SM2Utils.decrypt(reqStr, dcPrivateKey);
                } catch (Exception e) {
                    LOGGER.error(String.format("%s方法receiveSeqNo:%s解密请求报文时出现异常!请求报文:%s;电子证照私钥:%s", methodName, receiveSeqNo, reqStr, dcPrivateKey), e);
                    throw new DigitalException(String.format("解密请求报文时出现异常,请检查%s私钥或%s方使用的电子证照公钥是否配置有误或请求报文未加密!", receiverId, senderId), e);
                }
                LOGGER.info("{}方法receiveSeqNo:{}解密后的请求报文:{}", methodName, receiveSeqNo, decReqStr);

                if (StringUtils.isBlank(decReqStr)) {
                    throw new DigitalException("解密后的请求报文为空!");
                }

                //对解密后的请求体转为RequestMsg对象
                RequestMsg requestMsg = null;
                try {
                    requestMsg = JSON.parseObject(decReqStr, RequestMsg.class);
                } catch (Exception e) {
                    LOGGER.error("{}方法receiveSeqNo:{}解密后的http请求体格式不是一个标准JSON!解密后的请求体:{}", methodName, receiveSeqNo, decReqStr, e);
                    throw new DigitalException(String.format("解密后的http请求体格式不是一个标准JSON!"), e);
                }
                JSONArray data = null;
                try {
                    //data = (JSONArray)requestMsg.getData();
                    String jsonString = JSON.toJSONString(requestMsg.getData());
                    LOGGER.info("获取RequestMsg对象data属性数据为：" + jsonString);
                    String tempJsonStr = "";
                    if(jsonString.endsWith("\"")){
                        tempJsonStr = StringEscapeUtils.unescapeJava(jsonString.substring(1,jsonString.length()-1));
                    }else{
                        tempJsonStr = StringEscapeUtils.unescapeJava(jsonString);
                    }
                    data = JSONArray.parseArray(tempJsonStr);
                    LOGGER.info("请求报文data数据为：" + data);
                } catch (Exception e) {
                    throw new DigitalException(String.format("请求报文的data属性不是一个数组,请检查请求报文!"), e);
                }

                if (CollectionUtil.isEmpty(data)) {
                    throw new DigitalException(String.format("请求报文的data属性不能为空!"));
                }
                //将data属性值赋值给目标方法作为参数
                args[0] = JSON.toJSONString(data, SerializerFeature.WriteMapNullValue);
                //执行目标方法
                responseMsg = (ResponseMsg) joinPoint.proceed(args);
            }
            //设置响应消息的部分通用字段
            if (StringUtils.isBlank(responseMsg.getReceiveDate())) {
                responseMsg.setReceiveDate(receiveDate);
            }
            if (StringUtils.isBlank(responseMsg.getReceiveTime())) {
                responseMsg.setReceiveTime(receiveTime);
            }
            if (StringUtils.isBlank(responseMsg.getReceiverId())) {
                responseMsg.setReceiverId(receiverId);
            }
            if (responseMsg.getReceiveSeqNo() == null) {
                responseMsg.setReceiveSeqNo(receiveSeqNo);
            }
        } catch (DigitalException e) {
            responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_FAIL);
            responseMsg.setRtnMessage(e.getMessage());
            LOGGER.error("{}方法receiveSeqNo:{}出现异常", methodName, receiveSeqNo, e);
        } catch (Exception e) {
            responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_FAIL);
            responseMsg.setRtnMessage("电子证照服务内部发生异常:" + e.getMessage());
            LOGGER.error("{}方法receiveSeqNo:{}出现异常", methodName, receiveSeqNo, e);
        }

        //对响应体加密
        try {
            String resStr = null;
            resStr = responseMsg.toString();

            //TODO 下载文件,不对响应体打印日志
            if("cloudwindow-acceptance-3ps.cloudWindowService.commonService.getReceiveFiles".equals(method)){
                LOGGER.info("{}方法receiveSeqNo:{}提取收件影像,不对响应体明文打印日志", method, receiveSeqNo);
            }else{
                LOGGER.info("{}方法receiveSeqNo:{}响应给第三方的报文明文:{}", methodName, receiveSeqNo, resStr);
            }
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method mthd = methodSignature.getMethod();
            ResolveThirdParams annotation = mthd.getAnnotation(ResolveThirdParams.class);
            boolean encryptResponseFlag = annotation.encryptResponse();
            if(!encryptResponseFlag){
                LOGGER.info("{}方法receiveSeqNo:{}响应不加密,直接返回第三方", methodName, receiveSeqNo);
                return resStr;
            }
            //第三方公钥加密数据
            String encResStr = SM2Utils.encrypt(resStr, thirdPublicKey);
            LOGGER.info("{}方法receiveSeqNo:{}响应给第三方的报文密文:{}", method, receiveSeqNo, encResStr);
            return encResStr;
        } catch (Exception e) {
            LOGGER.error("{}方法receiveSeqNo:{}加密响应报文时出现异常,请检查{}的公钥是否配置有误!公钥:{}", methodName, receiveSeqNo, senderId, thirdPublicKey, e);
            //为了方便bug调试,响应加密出现异常,则直接返回明文提示加密错误
            responseMsg.setData(null);
            responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_FAIL);
            responseMsg.setRtnMessage(String.format("加密响应报文时出现异常,请检查%s的公钥是否配置有误!", senderId));
           /* if (mohurdIndustryCode.equals(industryCode)) {
                return responseMsg.toMohurdString();
            } else if (taxIndustryCode.equals(industryCode)) {
                return responseMsg.toTaxString();
            } else {
                return responseMsg.toString();
            }*/
            return responseMsg.toString();
        }
    }
}
