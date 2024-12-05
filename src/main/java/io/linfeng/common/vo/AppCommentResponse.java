/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版详情查看: https://www.linfengtech.cn
 * 商业版购买联系技术客服		QQ:  3582996245
 * 可正常分享和学习源码，不得转卖或非法牟利！
 * Copyright (c) 2021-2025 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.common.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.linfeng.modules.admin.entity.AppUserEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-25 18:51:22
 */
@Data
public class AppCommentResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;

	/**
	 * 父级id
	 */
	private Integer pid;

	/**
	 * 评论类型:1帖子
	 */
	private Integer type;
	/**
	 * 评论作者ID
	 */
	private Long uid;

	/**
	 * 被回复用户ID
	 */
	private Integer toUid;

	/**
	 * 评论帖子ID
	 */
	private Long postId;

	/**
	 * 评论内容
	 */
	private String content;

	/**
	 * 评论状态
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 子评论
	 */
	private List<Object> children;

	/**
	 * 评论用户信息
	 */
	private AppUserEntity userInfo;

	/**
	 * 点赞数
	 */
	private Integer thumbs;

	/**
	 * 评论是否点赞
	 */
	private Boolean isThumbs;
}
