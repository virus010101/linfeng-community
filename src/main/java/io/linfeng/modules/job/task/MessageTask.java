package io.linfeng.modules.job.task;

import cn.hutool.core.util.NumberUtil;
import io.linfeng.modules.admin.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Autowired
    @Lazy
    private MessageService messageService;


    @Override
    public void run(String params){
        logger.debug("messageTask定时任务正在执行，参数为：{}", params);
        //必须是整数而且为正数，代表清除几个月前的数据
        if(NumberUtil.isInteger(params)&&Integer.valueOf(params)>0){
            messageService.deleteMessageByMonth(Integer.valueOf(params));
        }
    }
}