package com.yushiji.code.common.core.utils;


import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MacUtil {

    // 1.获取客户端ip地址( 这个必须从客户端传到后台)：
    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) return "unknown";
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getRemoteAddr();

        if("0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip)){
            ip = "127.0.0.1";
        }else{
            ip = getMultistageReverseProxyIp(ip);
        }
        return getMacAddrByIp(ip);
    }
    /**
     * 从多级反向代理中获得第一个非unknown IP地址
     *
     * @param ip 获得的IP地址
     * @return 第一个非unknown IP地址
     */
    public static String getMultistageReverseProxyIp(String ip)
    {
        // 多级反向代理检测
        if (ip != null && ip.indexOf(",") > 0){
            final String[] ips = ip.trim().split(",");
            for (String subIp : ips){
                if (false == isUnknown(subIp)){
                    ip = subIp;
                    break;
                }
            }
        }
        return ip;
    }
    /**
     * 检测给定字符串是否为未知，多用于检测HTTP请求相关
     *
     * @param checkString 被检测的字符串
     * @return 是否未知
     */
    public static boolean isUnknown(String checkString){ return StringUtils.isBlank(checkString) || "unknown".equalsIgnoreCase(checkString);}

    // 2.获取客户端mac地址
    @SneakyThrows
    public static String getMacAddrByIp(String ip) {
        String macAddress = "";
        macAddress = getMacInWindows(ip).trim();
        if (macAddress == null || "".equals(macAddress)) {
            macAddress = getMacInLinux(ip).trim();
        }
        return macAddress;
    }


    public static String getMacInLinux(final String ip) {
        String result = "";
        String[] cmd = { "/bin/sh", "-c", "ping " + ip + " -c 2 && arp -a" };
        String cmdResult = callCmd(cmd);
        result = filterMacAddress(ip, cmdResult, ":");

        return result;
    }

    public static String getMacInWindows(final String ip) {
        String result = "";
        String[] cmd = { "cmd", "/c", "ping " + ip };
        String[] another = { "cmd", "/c", "arp -a" };
        String cmdResult = callCmd(cmd, another);
        result = filterMacAddress(ip, cmdResult, "-");
        return result;
    }

    public static String callCmd(String[] cmd) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String callCmd(String[] cmd, String[] another) {
        String result = "";
        String line = "";
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            proc.waitFor(); // 已经执行完第一个命令，准备执行第二个命令
            proc = rt.exec(another);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String filterMacAddress(final String ip, String sourceString, final String macSeparator) {
        String result = "";
        int index = sourceString.indexOf(ip);
        if (index == -1) {
            index = 0;
        }
        sourceString = sourceString.substring(index, sourceString.length() - 1);
        String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(sourceString);
        while (matcher.find()) {
            result = matcher.group(1);
            if (sourceString.indexOf(ip) <= sourceString.lastIndexOf(matcher.group(1))) {
                break; // 如果有多个IP,只匹配本IP对应的Mac.
            }
        }
        return result;
    }
}