package io.linfeng.common.utils;

/**
 * @author linfeng
 * @date 2022/1/20 13:15
 */
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class WechatUtil {

    /**
     * 获取小程序codeid换取openid
     * @param code
     * @return
     */
    public static JSONObject getOpenId(String code,String appId,String secret) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+ appId +"&secret=" +
                secret +"&js_code="+code+"&grant_type=authorization_code";
        PrintWriter out = null;
        BufferedReader in = null;
        String line;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            // 设置通用的请求属性 设置请求格式
            //设置返回类型
            conn.setRequestProperty("contentType", "text/plain");
            //设置请求类型
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //设置超时时间
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.setDoOutput(true);
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应    设置接收格式
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = in.readLine()) != null) {
                stringBuffer.append(line);
            }
            JSONObject jsonObject = JSONObject.parseObject(stringBuffer.toString());
            return jsonObject;

        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @Title: isEmpty
     * @Description: 判断对象是否为空
     * @param obj
     * @return
     * @return Integer
     */
    public static boolean isEmpty(Object obj) {
        return null == obj || "".equals(obj);
    }
}
