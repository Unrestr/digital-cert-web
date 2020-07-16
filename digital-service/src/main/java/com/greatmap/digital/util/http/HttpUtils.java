package com.greatmap.digital.util.http;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSONObject;
import com.greatmap.digital.excepition.DigitalException;
import com.greatmap.framework.core.io.IoUtil;
import com.greatmap.framework.core.util.CollectionUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gaorui
 * @create 2019-12-10 9:34
 */
@Component
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 发送url Get 请求
     *
     * @param url 请求参数拼接在URL后面
     * @return
     */
    public static ResponseMsgDto httpGet(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseMsgDto responseMsgDto = new ResponseMsgDto();
        //响应报文体
        String resStr = null;
        CloseableHttpResponse httpResponse = null;
        try {
            HttpGet get = new HttpGet(url);
            httpResponse = httpClient.execute(get);
            responseMsgDto.setCode(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
            HttpEntity entity = httpResponse.getEntity();
            if (null != entity) {
                byte[] data = transformInputstream(entity.getContent());
                resStr = new String(data, StandardCharsets.UTF_8);
                logger.info("http响应体:{};请求地址:{}", resStr, url);
                responseMsgDto.setData(resStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("http响应失败,错误信息:{};请求地址:{}", e.getMessage(), url);
            responseMsgDto.setMsg(url + e.getMessage());
        } finally {
            IOUtils.closeQuietly(httpClient);
            IOUtils.closeQuietly(httpResponse);
        }
        return responseMsgDto;
    }

    /**
     * 发送post HTTP请求
     *
     * @param postUrl 请求URL
     * @param params  请求参数
     * @param name    文件名
     * @param file    文件
     * @return
     */
    public static byte[] httpPost(String postUrl, Map<String, Object> params, String name, File file) {
        // 返回的字符串
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(postUrl);
        postRequest.setHeader("Accept", "application/json;charset=utf-8");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMimeSubtype("form-data");
        // 设置字符编码
        builder.setCharset(StandardCharsets.UTF_8);
        // 准备请求参数 设置请求参数编码
        if (CollectionUtil.isNotEmpty(params)) {
            params.entrySet().stream()
                    .filter(item -> StringUtils.isNotEmpty(item.getKey()) && ObjectUtil.isNotEmpty(item.getValue()))
                    .forEach(item -> builder.addTextBody(item.getKey(), item.getValue().toString(), ContentType.APPLICATION_JSON.withCharset(StandardCharsets.UTF_8)));
        }
        //设置文件
        if (StringUtils.isNotBlank(name) && file != null) {
            builder.addBinaryBody(name, file);
        }
        HttpEntity reqEntity = builder.build();
        CloseableHttpResponse response = null;
        InputStream postIps = null;
        int code = 500;
        try {
            // 整体发送到服务端
            postRequest.setEntity(reqEntity);
            response = httpClient.execute(postRequest);
            //获取响应码
            code = response.getStatusLine().getStatusCode();
            postIps = response.getEntity().getContent();
            if (code == HttpStatus.HTTP_OK) {
                return IoUtil.readBytes(postIps);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DigitalException("电子证照服务请求错误,错误码：" + code + "地址：" + postUrl,e);
        } finally {
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
        return null;
    }

    /**
     * 调用http接口
     *
     * @param jObject 请求参数
     * @param url     请求路径
     * @return
     */
    public static ResponseMsgDto post(JSONObject jObject, String url) {
        ResponseMsgDto responseMsgDto = new ResponseMsgDto();
        InputStream in = null;
        ByteArrayOutputStream barray = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            responseMsgDto.setCode(String.valueOf(conn.getResponseCode()));
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.write(jObject.toString().getBytes(StandardCharsets.UTF_8));
            // 接收返回信息
            in = new DataInputStream(conn.getInputStream());
            byte[] array = new byte[4096];
            int count = -1;
            barray = new ByteArrayOutputStream();
            while (-1 != (count = in.read(array))) {
                barray.write(array, 0, count);
            }
            byte[] data = barray.toByteArray();
            responseMsgDto.setData(new String(data, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
            responseMsgDto.setMsg(url + e.getMessage());
        } finally {
            IoUtil.close(barray);
            IoUtil.close(in);
        }
        return responseMsgDto;
    }

    /**
     * 将输入流转为字节数组
     *
     * @param input 输入流
     * @return 返回字节数组
     * @throws Exception
     */
    public static byte[] transformInputstream(InputStream input) throws Exception {
        byte[] byt = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b = 0;
        b = input.read();
        while (b != -1) {
            baos.write(b);
            b = input.read();
        }
        byt = baos.toByteArray();
        return byt;
    }

    public static String httpPost2(String postUrl, Map<String, Object> map) {
        // 返回的字符串
        StringBuffer bs = new StringBuffer();

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(postUrl);
        // 解决了返回值中文乱码的问题
        postRequest.setHeader("Accept", "application/json; charset=utf-8");

        List<NameValuePair> list = new ArrayList<>();
        try {
            if (map != null && map.size() > 0) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (entry.getValue() != null && StringUtils.isNotEmpty(entry.getKey())) {
                        list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                    }
                }
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
                postRequest.setEntity(entity);
            }
            CloseableHttpResponse response = httpClient.execute(postRequest);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                InputStream postIps = response.getEntity().getContent();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(postIps, StandardCharsets.UTF_8));
                String l = null;
                while ((l = buffer.readLine()) != null) {
                    bs.append(l);
                }
                return bs.toString();
            }
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return bs.toString();
    }


}
