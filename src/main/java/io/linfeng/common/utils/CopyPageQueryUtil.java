package io.linfeng.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询复制响应对象封装
 * @author linfeng
 * @date 2022/1/23 18:02
 */
public class CopyPageQueryUtil {

    public static AppPageUtils copyPageQuery(AppPageUtils page, Object object) {
        List<?> list = page.getData();
        List<Object> responseList = new ArrayList<>();
        list.forEach(l -> {
            Object objects = new Object();
            BeanUtils.copyProperties(l, objects);
            responseList.add(objects);
        });
        page.setData(responseList);
        return page;
    }

    public static List<?> copyPageQueryList(AppPageUtils page, Object object) {
        List<?> list = page.getData();
        List<Object> responseList = new ArrayList<>();
        list.forEach(l -> {
            Object objects = new Object();
            BeanUtils.copyProperties(l, objects);
            responseList.add(objects);
        });
        return responseList;
    }
}
