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
