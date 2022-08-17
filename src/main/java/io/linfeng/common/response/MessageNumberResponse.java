/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版演示站点: https://www.linfeng.tech
 * 商业版购买联系技术客服	 QQ:  3582996245
 * 可正常分享和学习源码，不得转卖或非法牟利！
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
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
