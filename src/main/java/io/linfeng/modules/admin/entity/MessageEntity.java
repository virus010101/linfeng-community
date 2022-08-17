/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版演示站点: https://www.linfeng.tech
 * 商业版购买联系技术客服
 * QQ: 3582996245
 * 可正常分享和学习源码，不得专卖或非法牟利！
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-26 13:15:30
 */
@Data
@TableName("lf_message")
public class MessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 消息id
	 */
	@TableId
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

}
