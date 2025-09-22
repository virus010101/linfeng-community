package io.linfeng.common.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * IP地址
 *
 */
@Slf4j
public class IPUtils {
    // IP归属地查询
    private static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";
	/**
	 * 获取IP地址
	 * 
	 * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
    	String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }
        
        return ip;
    }

    /**
     * 根据ip获取归属地 地址1
     * @param ip 用户ip
     * @return 地址
     */
    public static String getCityInfo(String ip) {
        String api = String.format(IP_URL, ip);

        String s = null;
        try {
            s = HttpUtil.get(api, 5000);
        } catch (Exception e) {
            log.error("HTTP请求异常，IP: {}, 错误: {}", ip, e.getMessage());
            return "";
        }

        if(s == null || s.isEmpty()){
            return "";
        }

        JSONObject object = null;
        try {
            object = JSONUtil.parseObj(s);
        } catch (Exception e) {
            log.error("获取地理位置异常 {}", ip);
            return "";
        }

        if(object == null){
            return "";
        }

        String addr = object.get("addr", String.class);
        if(!ObjectUtil.isEmpty(addr)){
            return addr.split(" ")[0];
        }
        return addr;
    }
}
