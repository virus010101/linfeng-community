package io.linfeng.common.response;

import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.entity.MessageEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author linfeng
 * @date 2022/1/28 16:47
 */
@Data
public class ChatMessageListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer count;

    private AppUserEntity userInfo;

    private MessageEntity msg;
}
