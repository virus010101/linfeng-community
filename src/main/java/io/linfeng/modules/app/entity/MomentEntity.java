/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 演示站点:https://www.linfeng.tech
 * 可正常分享和学习源码，不得用于非法牟利！
 * 商业版购买联系技术客服  QQ: 3582996245
 *  Copyright (c) 2021-2023 linfeng all rights reserved.
 * 版权所有，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.app.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 动态表
 * 
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-05 14:23:11
 */
@Data
@TableName("tb_moment")
public class MomentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 发送者id
	 */
	private Long userId;
	/**
	 * 类型（TEXT：纯文本，IMAGE：图片，VIDEO：视频）
	 */
	private String type;
	/**
	 * 文本内容
	 */
	private String textContent;
	/**
	 * 活动id
	 */
	private Long activityId;
	/**
	 * 点赞数量
	 */
	private Integer likeCount;
	/**
	 * 评论数量
	 */
	private Integer commentCount;
	/**
	 * 转发数量
	 */
	private Integer repostCount;
	/**
	 * 分享数量
	 */
	private Integer shareCount;
	/**
	 * 查看次数
	 */
	private Integer viewCount;
	/**
	 * 转发的动态id
	 */
	private Long repostMomentId;
	/**
	 * 状态（0：已屏蔽，1：正常，2：已删除）
	 */
	private Integer status;
	/**
	 * 
	 */
	private Long artistId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
