
package io.linfeng.common.utils;

/**
 * Redis所有Keys
 *
 */
public class RedisKeys {

    public static String getSysConfigKey(String key){
        return "sys:config:" + key;
    }

    public static String getUserKey(Integer userId){
        return "userId:" + userId;
    }
}
