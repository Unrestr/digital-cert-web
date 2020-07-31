package com.greatmap.digital.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Ip 工具.
 *
 * @author : frm
 * @date : Created in 2018/10/18 10:50 AM
 */
@Slf4j
public class IpUtil {

    /**
     * 本机默认IP
     */
    private static final String LOCALHOST = "127.0.0.1";

    /**
     * HTTP中未知IP标识
     */
    private static final String UNKNOWN_IP = "unknown";

    /**
     * IP分隔符
     */
    private static final String IP_SEPARATOR = ",";

    /**
     * Header 头中的 IP 地址标识
     * HTTP_X_FORWARDED_FOR 阿里云代理使用标识
     */
    private static final String[] IP_KEYS = new String[]{"REMOTE-IP", "REMOTE-ADDR",
            "X-FORWARDED-FOR", "X-FORWARDED", "FORWARDED-FOR", "FORWARDED", "CLIENT-IP",
            "X-CLUSTER-CLIENT-IP"};

    /**
     * 获取本地IP
     *
     * @return .
     */
    public static String getLocalIp() {
        String localIp = LOCALHOST;

        try {
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            log.error("获取本地IP失败，错误原因：" + e.getMessage());
        }

        return localIp;
    }

    /**
     * 获取内网IP
     *
     * @return .
     */
    public static String getInnerIp() {
        // 内网IP
        String innerIp = null;
        // 外网IP
        String outerIp = null;
        Enumeration<NetworkInterface> netInterfaces = null;

        try {
            // 获取所有网卡
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            log.warn(e.getMessage());
        }

        // 网卡IP
        InetAddress ip;
        if (netInterfaces != null) {
            while (netInterfaces.hasMoreElements()) {
                // 当前网卡
                NetworkInterface ni = netInterfaces.nextElement();
                // 当前网卡地址
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = address.nextElement();
                    if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                            && !ip.getHostAddress().contains(":")) {
                        if (isReservedIP(ip.getHostAddress())) {
                            innerIp = ip.getHostAddress();
                        } else {
                            outerIp = ip.getHostAddress();
                        }
                    }
                }
            }
        }

        return innerIp != null? innerIp: outerIp;
    }

    /**
     * 获取ip  先获取eth0网卡的ip，获取不到再获取内网ip，获取不到获取第一个IP，都没有则返回127.0.0.1
     * @return .
     */
    public static String getIp() {
        String ip = null;
        try {
            ip = getHostAddress("eth0");
            if (org.apache.commons.lang3.StringUtils.isBlank(ip)) {
                ip = getHostAddress(null);
            }
        } catch (Exception e) {
            log.error("获取本地IP失败，错误原因：{}" + e.getMessage());
        }
        return ip != null ? ip : "127.0.0.1";
    }

    /**
     * 获取已激活网卡的IP地址
     *
     * @param interfaceName 可指定网卡名称,null则先获取内网ip获取不到获取第一张
     * @return ip
     */
    private static String getHostAddress(String interfaceName) throws SocketException {
        String innerIp = null;
        String outerIp = null;
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ni = interfaces.nextElement();
            Enumeration<InetAddress> allAddress = ni.getInetAddresses();
            while (allAddress.hasMoreElements()) {
                InetAddress address = allAddress.nextElement();
                if (address.isLoopbackAddress()) {
                    // skip the loopback addr
                    continue;
                }
                if (address instanceof Inet6Address) {
                    // skip the IPv6 addr
                    continue;
                }
                String ip = address.getHostAddress();
                if (null == interfaceName) {
                    // 判断内网IP
                    if (isReservedIP(ip)) {
                        innerIp = ip;
                    }else {
                        outerIp = ip;
                    }
                } else if (interfaceName.trim().equals(ni.getDisplayName())) {
                    innerIp = ip;
                }
            }
        }
        return innerIp != null ? innerIp: outerIp;
    }

    /**
     * 获取请求来源Ip
     *
     * @param request 请求信息
     */
    public static String getRequestIp(HttpServletRequest request) {
        String ip = "";
        if (request == null) {
            return ip;
        }

        try {
            for (String ipKey : IP_KEYS) {
                if (request.getHeader(ipKey) == null) {
                    continue;
                }

                ip = request.getHeader(ipKey);
                if (ip.length() > 15 && ip.indexOf(IP_SEPARATOR) > 0) {
                    ip = ip.substring(0, ip.indexOf(IP_SEPARATOR));
                }

                if (ip.length() > 0 && !isReservedIP(ip)) {
                    return ip;
                }
            }

            return ip;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * TODO 判断是否为保留 IP 地址 保留 IP 地址：
     * 10.0.0.0/8
     * 100.64.0.0/10
     * 127.0.0.1/8
     * 172.16.0.0/16
     * 192.168.0.0/16
     *
     * @param ip ip地址
     *
     * @return .
     */
    public static boolean isReservedIP(String ip) {
        long ipNum = getIpNum(ip);

        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");
        long bBegin = getIpNum("127.0.0.0");
        long bEnd = getIpNum("127.255.255.255");
        long cBegin = getIpNum("100.64.0.0");
        long cEnd = getIpNum("100.127.255.255");
        long dBegin = getIpNum("172.16.0.0");
        long dEnd = getIpNum("172.31.255.255");
        long eBegin = getIpNum("192.168.0.0");
        long eEnd = getIpNum("192.168.255.255");

       return isReservedIP(ipNum, aBegin, aEnd)
               || isReservedIP(ipNum, bBegin, bEnd)
               || isReservedIP(ipNum, cBegin, cEnd)
               || isReservedIP(ipNum, dBegin, dEnd)
               || isReservedIP(ipNum, eBegin, eEnd);
    }

    /**
     * 是否是内网IP
     *
     * @param ip    请求 IP 数值
     * @param begin 内网 IP 开始值
     * @param end   内网 IP 结束值
     *
     * @return .
     */
    private static boolean isReservedIP(long ip,long begin,long end) {
        return (ip >= begin) && (ip <= end);
    }

    /**
     * 获取当前 IP 转换数值
     *
     * @param ip IP 地址
     *
     * @return .
     */
    private static long getIpNum(String ip) {
        String [] ipGroup = ip.trim().split("\\.");
        try {
            long a = Integer.parseInt(ipGroup[0]);
            long b = Integer.parseInt(ipGroup[1]);
            long c = Integer.parseInt(ipGroup[2]);
            long d = Integer.parseInt(ipGroup[3]);
            return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
        } catch (Exception e) {
            log.error("非法IP格式：", ip);
            return 0;
        }
    }
}
