package io.linfeng.common.response;

import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.entity.MessageEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linfeng
 * @date 2022/2/8 21:25
 */
@Data
public class MessageNoticeResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer mId;
    /**
     * 发送者uid
     */
    private Integer fromUid;
    /**
     * 接收者uid
     */
    private Integer toUid;
    /**
     * 帖子id
     */
    private Integer postId;
    /**
     * 推送标题
     */
    private String title;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 0未读，1已读
     */
    private Integer status;
    /**
     * 1为点赞，2为评论  3为收藏 4为关注  5为推送文章 6私聊
     */
    private Integer type;
    /**
     * 创建时间
     */
    private Date createTime;

    private AppUserEntity userInfo;

}
