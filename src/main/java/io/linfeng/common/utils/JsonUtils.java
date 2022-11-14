package io.linfeng.common.utils;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linfeng
 * @date 2022/1/25 15:37
 */
public  class JsonUtils {


    public static List<String> JsonToList(String jsonString){
        return JSON.parseArray(jsonString, String.class);
    }
}
