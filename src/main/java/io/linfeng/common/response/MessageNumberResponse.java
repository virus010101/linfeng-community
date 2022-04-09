package io.linfeng.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class MessageNumberResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 总消息数量
	 */
	private Integer allCount;
	/**
	 * 评论数
	 */
	private Integer comment;
	/**
	 * 关注数
	 */
	private Integer follow;
	/**
	 * 帖子评论点赞数
	 */
	private Integer thumbCount;

	/**
	 * 推送通知消息
	 */
	private List<MessageNoticeResponse> articleMsgList;


	private List<ChatMessageListResponse> chatMsgList;


}
