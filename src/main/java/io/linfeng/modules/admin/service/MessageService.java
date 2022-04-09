package io.linfeng.modules.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.response.MessageListResponse;
import io.linfeng.common.response.MessageNumberResponse;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.entity.MessageEntity;
import io.linfeng.modules.app.form.UpdateChatStatusForm;
import io.linfeng.modules.app.form.UpdateSystemNoticeStatusForm;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-26 13:15:30
 */
public interface MessageService extends IService<MessageEntity> {

    PageUtils queryPage(Map<String, Object> params);

    @Async
    void sendMessage(Integer fromUid,Integer toUid,Integer postId,Integer type,String content,String title);

    void sendMessageNotAsync(Integer fromUid,Integer toUid,Integer postId,Integer type,String content,String title);

    Boolean status(Integer type, Integer uid);

}

