package io.linfeng.common.utils;

import io.linfeng.common.exception.LinfengException;

/**
 * @author linfeng
 * @date 2022/2/21 10:38
 */
public class FileCheckUtil {

    public static void checkSize(long maxSize, long size) {
        // 单位 M
        int len = 1024 * 1024;
        if(size > (maxSize * len)){
            throw new LinfengException("上传文件超出规定大小");
        }
    }
}
