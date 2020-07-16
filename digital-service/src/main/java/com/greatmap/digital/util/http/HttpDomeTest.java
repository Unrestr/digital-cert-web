package com.greatmap.digital.util.http;

import org.junit.Test;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author gaorui
 * @create 2019-12-10 10:31
 */
public class HttpDomeTest {

    @Test
    public void testGet(){
        ResponseMsgDto responseMsgDto = HttpUtils.httpGet("https://www.baidu.com/");
        System.out.println(responseMsgDto.toString());
    }

    @Test
    public void testPost(){
        HashMap<String, Objects> paramMap = new HashMap<>(16);
       // paramMap.put("city",new Objects());
        //ResponseMsgDto responseMsgDto = HttpUtil.httpPost("https://www.baidu.com/",paramMap,null,null);
      //  System.out.println(responseMsgDto.toString());
    }
}
