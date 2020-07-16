package com.greatmap.digital.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.greatmap.digital.excepition.DigitalException;
import com.greatmap.digital.util.CodeGeneratorUtil;
import com.greatmap.framework.core.date.DateUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: DexHttpClient
 * @Description 调用数据共享交换系统
 * @author: sms
 * @DATE: 2019/10/23 10:14
 * @Version: 1.0
 **/
@Component
public class DexHttpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(DexHttpClient.class);

    @Value("${dex.dexSenderPublicKeyUrl}")
    private String dexSenderPublicKeyUrl;
    @Value("${dex.dexSenderIndustryCodeUrl}")
    private String dexSenderIndustryCodeUrl;
    @Value("${dex.dexPublicKeyUrl}")
    private String dexPublicKeyUrl;


    /**
     * 服务方获取请求方公钥
     * @param senderID 请求方标识
     * @return
     */
    public String getSenderPublicSecret(String senderID){
        if(StringUtils.isBlank(senderID)){
            throw new DigitalException(String.format("请求方标识为空！"));
        }

        HttpGet httpGet = new HttpGet();
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("senderID",senderID));
        String params = "";
        try {
            params = EntityUtils.toString(new UrlEncodedFormEntity(basicNameValuePairs, Consts.UTF_8));
        } catch (Exception e) {
            throw new DigitalException(String.format("获取请求方公钥转换GET请求参数失败！"), e);
        }

        String url = dexSenderPublicKeyUrl;

        if(StringUtils.isNotEmpty(params)){
            url = String.format("%s?%s",url,params);
        }
        try {
            httpGet.setURI(new URI(url));
        } catch (URISyntaxException e) {
            throw new DigitalException(String.format("获取请求方公钥GET请求封装URI对象失败！"), e);
        }

        String context = "";
        HttpClient httpClient = HttpClientBuilder.create().build();

        httpGet.setHeader("Content-type","application/json;charset=utf-8");
        HttpResponse response = null;
        InputStream inputStream = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                inputStream = entity.getContent();
                byte[] data = transformInputstream(inputStream);
                context = new String(data,"utf-8");
            }
        }catch (DigitalException e){
            throw new DigitalException(e.getMessage(), e);
        }catch (HttpHostConnectException e){
            throw new DigitalException(url+"请求拒绝连接！", e);
        }catch (ConnectTimeoutException e){
            throw new DigitalException(url+"请求连接超时！", e);
        }catch (SocketTimeoutException e){
            throw new DigitalException(url+"请求响应超时！", e);
        }catch (Exception e){
            throw new DigitalException(url+"请求异常，"+ e.getMessage(), e);
        }finally {
            IOUtils.closeQuietly(inputStream);
        }
        return context;
    }

    /**
     * 获取请求方行业编码
     * @param senderID 请求方标识
     * @return
     */
    public String getSenderIndustryCode(String senderID){
        if(StringUtils.isBlank(senderID)){
            return "";
        }

        HttpGet httpGet = new HttpGet();
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("systemCode",senderID));
        String params = "";
        try {
            params = EntityUtils.toString(new UrlEncodedFormEntity(basicNameValuePairs, Consts.UTF_8));
        } catch (IOException e) {
            throw new DigitalException(String.format("转换GET请求参数失败！"));
        }

        String url = dexSenderIndustryCodeUrl;

        if(StringUtils.isNotEmpty(params)){
            url = String.format("%s?%s",url,params);
        }
        try {
            httpGet.setURI(new URI(url));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new DigitalException(String.format("GET请求封装URI对象失败！"));
        }

        String context = "";
        HttpClient httpClient = HttpClientBuilder.create().build();

        httpGet.setHeader("Content-type","application/json;charset=utf-8");
        HttpResponse response = null;
        InputStream inputStream = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                inputStream = entity.getContent();
                byte[] data = transformInputstream(inputStream);
                context = new String(data,"utf-8");
                if(StringUtils.isNotBlank(context)){
                    JSONObject jsonObject = JSONObject.parseObject(context);
                    context = jsonObject.getString("data");
                    jsonObject = JSONObject.parseObject(context);
                    context = jsonObject.getString("industryCode");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(inputStream);
        }
        return context;
    }


    /**
     * 请求方获取服务方公钥
     *
     * @param areaCode     行政区划代码
     * @param industryCode 行业代码
     * @param method       方法名
     * @return
     */
    public String getReceiverPublicSecret(String areaCode, String industryCode, String method) {
        if (StringUtils.isBlank(areaCode) || StringUtils.isBlank(industryCode) || StringUtils.isBlank(method)) {
            throw new DigitalException(String.format("areaCode,industryCode,method参数有为空的"));
        }
        LOGGER.info("请求方获取服务方公钥" + String.format("%s--->%s--->%s", areaCode, industryCode, method));
        HttpGet httpGet = new HttpGet();
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("areaCode", areaCode));
        basicNameValuePairs.add(new BasicNameValuePair("industryCode", industryCode));
        basicNameValuePairs.add(new BasicNameValuePair("method", method));
        String params = "";
        try {
            params = EntityUtils.toString(new UrlEncodedFormEntity(basicNameValuePairs, Consts.UTF_8));
        } catch (Exception e) {
            throw new DigitalException(String.format("获取服务方公钥转换GET请求参数失败！"), e);
        }

        String url = dexPublicKeyUrl;

        if (StringUtils.isNotEmpty(params)) {
            url = String.format("%s?%s", url, params);
            LOGGER.info("请求方获取服务方公钥访问路径：" + url);
        }
        try {
            httpGet.setURI(new URI(url));
        } catch (URISyntaxException e) {
            throw new DigitalException(String.format("获取服务方公钥GET请求封装URI对象失败！"), e);
        }

        String context = "";
        HttpClient httpClient = HttpClientBuilder.create().build();

        httpGet.setHeader("Content-type", "application/json;charset=utf-8");
        HttpResponse response = null;
        InputStream inputStream = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                inputStream = entity.getContent();
                byte[] data = transformInputstream(inputStream);
                context = new String(data, "utf-8");
                LOGGER.info("请求方获取服务方公钥返回值：" + context);
            }
        } catch (DigitalException e) {
            throw new DigitalException(e);
        } catch (HttpHostConnectException e) {
            throw new DigitalException(url + "请求拒绝连接！", e);
        } catch (ConnectTimeoutException e) {
            throw new DigitalException(url + "请求连接超时！", e);
        } catch (SocketTimeoutException e) {
            throw new DigitalException(url + "请求响应超时！", e);
        } catch (Exception e) {
            throw new DigitalException(url + "请求异常，" + e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return context;
    }
    /**
     * 将输入流转为字节数组
     * @param input 输入流
     * @return 返回字节数组
     * @throws Exception
     */
    public static byte[] transformInputstream(InputStream input)throws Exception{
        byte[] byt = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b = 0;
        b = input.read();
        while( b != -1) {
            baos.write(b);
            b = input.read();
        }
        byt = baos.toByteArray();
        return byt;
    }

}
