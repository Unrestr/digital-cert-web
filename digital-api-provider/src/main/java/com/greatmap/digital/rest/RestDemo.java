package com.greatmap.digital.rest;

import com.greatmap.digital.util.http.HttpUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gaorui
 * @create 2020-01-13 11:02
 */
public class RestDemo {

    public void testAddCertificate() {

        String url = "http://192.168.5.34:58080/digital-provider/rest/api/addCert";
        String params = "{\n" +
                "    \"zzmc\":\"单一版不动产权证书\",\n" +
                "    \"qxdm\":\"650000\"\n" +
                "}";
     // String pdfFile = "C:\\FTP\\work\\print\\estate\\documents\\register\\2312\\test.pdf";
      //  File file = new File(pdfFile);
        Map<String,Object> map = new HashMap<>();
        map.put("requestData",params);
        byte[] pdfFiles = HttpUtils.httpPost(url, map, null, null);
      //  assert pdfFiles != null;
        String s = new String(pdfFiles, StandardCharsets.UTF_8);
        System.out.println(s);
    }

    public void testLogoutCert() {
        String url = "http://192.168.5.51:8081/digital-provider/rest/api/logoutCert";
        Map<String,Object> map = new HashMap<>();
        map.put("zzmc","不动产权证书");
        map.put("zzbh","aaaaaaaa");
        map.put("zxyy","注销Demo");
        map.put("sign","不动产权登记注销于");
        byte[] pdfFiles = HttpUtils.httpPost(url, map, null, null);
        assert pdfFiles != null;
        String s = new String(pdfFiles, StandardCharsets.UTF_8);
        System.out.println(s);
    }


    public void testVerifyCertificate() {
        String url = "http://192.168.0.103:8081/digital-provider/rest/api/verifyCertificate";
        Map<String,Object> map = new HashMap<>();
        map.put("zzmc","不动产权证书");
        map.put("zzbh","冀（2019）临漳县不动产权第0001169号");
        map.put("czzt","马俊峰");
        String pdfFile = "C:\\FTP\\work\\print\\estate\\documents\\register\\2312\\测试一下.pdf";
        File file = new File(pdfFile);
        byte[] pdfFiles = HttpUtils.httpPost(url, map, "pdfFile", file);
        assert pdfFiles != null;
        String s = new String(pdfFiles, StandardCharsets.UTF_8);
        System.out.println(s);
    }


    public void testVerifyInvalidCert() {
        String url = "http://192.168.5.51:8081/digital-provider/rest/api/invalidCert";
        Map<String,Object> map = new HashMap<>(10);
        map.put("zzbh","31635465");
        byte[] pdfFiles = HttpUtils.httpPost(url, map, null,null);
        assert pdfFiles != null;
        String s = new String(pdfFiles, StandardCharsets.UTF_8);
        System.out.println(s);
    }


    public void testFindCertInfoHisPage() {
        String url = "http://192.168.5.51:8081/digital-provider/rest/api/findCertInfoHisPage";
        Map<String,Object> map = new HashMap<>(10);
        map.put("nCurrent",0);
        map.put("nSize",10);
        map.put("qxdm","420000");
        byte[] pdfFiles = HttpUtils.httpPost(url, map, null,null);
        assert pdfFiles != null;
        String s = new String(pdfFiles, StandardCharsets.UTF_8);
        System.out.println(s);
    }


    public void testFindTest() {
        String url = "http://192.168.5.51:8081/digital-provider/rest/api/test";
        Map<String,Object> map = new HashMap<>(10);
        map.put("nCurrent",0);
        map.put("nSize",10);
        map.put("qxdm","420000");
        byte[] pdfFiles = HttpUtils.httpPost(url, map, null,null);
        assert pdfFiles != null;
        String s = new String(pdfFiles, StandardCharsets.UTF_8);
        System.out.println(s);
    }
}
