/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版详情查看: https://www.linfengtech.cn
 * 商业版购买联系技术客服	 QQ:  3582996245
 * 可正常分享和学习源码，不得转卖或非法牟利！
 * Copyright (c) 2021-2025 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.job.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * 定期清理所有消息定时任务
 *
 *
 * @author linfeng
 * @date 2022/5/22 22:43
 */
@Component("messageTask")
public class MessageTask implements ITask {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void run(String params){
        logger.debug("messageTask定时任务正在执行，参数为：{}", params);
    }
}