package com.greatmap.digital.intercepters;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.google.common.collect.Lists;
import com.greatmap.dex.model.DexRequestBody;
import com.greatmap.dex.model.DexResponseBody;
import com.greatmap.dex.service.httpclient.DexHttpClient;
import com.greatmap.dex.service.httpserver.DexHttpServer;
import com.greatmap.digital.excepition.DigitalThirdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


/**
 * @author gr
 * @since 2020 7 20
 */
@Component("httpFactory")
public class DexHttpFactory implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(DexHttpFactory.class);
    /**
     * 请求实例容器
     */
    private Map<String, DexHttpClient> dexHttpClientMap;

    /**
     * 响应实例容器
     */
    private Map<String, DexHttpServer> dexHttpServerMap;

    @Value("${tempDirectory}")
    private String tempDirectory;

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //获取请求实例
        dexHttpClientMap = event.getApplicationContext().getBeansOfType(DexHttpClient.class);
        //获取响应实例
        dexHttpServerMap = event.getApplicationContext().getBeansOfType(DexHttpServer.class);
    }


    /**
     * 根据名称查询请求实例对象
     *
     * @param name
     * @return
     */
    public DexHttpClient findDexHttpClient(String name) {
        if (dexHttpClientMap == null || !dexHttpClientMap.containsKey(name)) {
            return null;
        }
        return dexHttpClientMap.get(name);
    }

    /**
     * 根据名称查询响应实例对象
     *
     * @param name
     * @return
     */
    public DexHttpServer findDexHttpServer(String name) {
        if (dexHttpServerMap == null || !dexHttpServerMap.containsKey(name)) {
            return null;
        }
        return dexHttpServerMap.get(name);
    }

    /**
     * 请求DEX
     *
     * @param params
     * @param url
     * @return
     */
    public DexResponseBody getDexResponseBody(Map<String, Object> params, String url) {
        DexRequestBody dexRequestBody = new DexRequestBody();
        dexRequestBody.setData(params);
        dexRequestBody.setSendSeqNo(IdWorker.getId());
        dexRequestBody.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss")));
        dexRequestBody.setSendDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        DexHttpClient dexHttpClient = findDexHttpClient(url);
        if (dexHttpClient == null) {
            throw new DigitalThirdException("未找到对应的请求服务");
        }
        DexResponseBody dexResponseBody = dexHttpClient.execute(dexRequestBody);
        if ("0".equals(dexResponseBody.getTxStatus())) {
            throw new DigitalThirdException("请求服务失败！" + url);
        }
        return dexResponseBody;
    }

    /**
     * 一窗请求
     * @param params
     * @param url
     * @return
     */
    public DexResponseBody getDexResponseBodyOneWindow(Map<String, Object> params, String url) {
        List<Map<String, Object>> paramsList = Lists.newArrayList();
        paramsList.add(params);
        DexRequestBody dexRequestBody = new DexRequestBody();
        dexRequestBody.setData(paramsList);
        dexRequestBody.setSendSeqNo(IdWorker.getId());
        dexRequestBody.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss")));
        dexRequestBody.setSendDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        DexHttpClient dexHttpClient = findDexHttpClient(url);
        if (dexHttpClient == null) {
            throw new DigitalThirdException("未找到对应的请求服务");
        }
        DexResponseBody dexResponseBody = dexHttpClient.execute(dexRequestBody);
        logger.info("=====返回数据="+ JSONObject.toJSONString(dexResponseBody));
        if ("0".equals(dexResponseBody.getTxStatus())) {
            throw new DigitalThirdException("请求服务失败！" + url);
        }
        return dexResponseBody;
    }

    /**
     * 一窗请求
     *
     * @param params
     * @param url
     * @return
     */
    public File getDexResponseBodyOneWindowWithFile(Map<String, String> params, String url) {
        List<Map<String, String>> paramsList = Lists.newArrayList();
        paramsList.add(params);
        DexRequestBody dexRequestBody = new DexRequestBody();
        dexRequestBody.setData(paramsList);
        dexRequestBody.setSendSeqNo(IdWorker.getId());
        dexRequestBody.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss")));
        dexRequestBody.setSendDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        DexHttpClient dexHttpClient = findDexHttpClient(url);
        if (dexHttpClient == null) {
            throw new DigitalThirdException("未找到对应的请求服务");
        }
        File file = dexHttpClient.executeResponseFile(dexRequestBody, tempDirectory+"/11.zip");
        if (file != null && file.exists()) {
            return file;
        }
        throw new DigitalThirdException("获取文件信息失败！" + url);
    }
}
