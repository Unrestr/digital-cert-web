package com.greatmap.digital.util;


import javax.servlet.http.HttpServletRequest;

/**
 * @author zhaopc
 * @date 2018/6/8 14:26
 */
public class IpUtils {

    private static String UN_KNOWN = "unknown";

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-real-ip");

        if (ip == null || ip.length() == 0 || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
            if (ip != null) {
                ip = ip.split(",")[0].trim();
            }
        }

        if (ip == null || ip.length() == 0 || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || UN_KNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

}
