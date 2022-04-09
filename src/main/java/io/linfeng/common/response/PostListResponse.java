package io.linfeng.common.response;

import io.linfeng.modules.admin.entity.AppUserEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class PostListResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Integer id;
	/**
	 * 用户id
	 */
	private Integer uid;
	/**
	 * 圈子id
	 */
	private Integer topicId;
	/**
	 * 话题id
	 */
	private Integer discussId;
	/**
	 * 投票id
	 */
	private Integer voteId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 文件
	 */
//	private String media;
	List<String> media;
	/**
	 * 浏览量
	 */
	private Integer readCount;
	/**
	 * 置顶
	 */
	private Integer postTop;
	/**
	 * 帖子类型：1 图文 ，2视频 ，3文章，4投票
	 */
	private Integer type;
	/**
	 * 地址名称
	 */
	private String address;
	/**
	 * 经度
	 */
	private Double longitude;
	/**
	 * 纬度
	 */
	private Double latitude;
	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 点赞数
	 */
	private Integer collectionCount;
	/**
	 * 评论数
	 */
	private Integer commentCount;

	/**
	 * 用户信息
	 */
	private AppUserEntity userInfo;

	/**
	 * 话题
	 */
	private String discussTitle;

	/**
	 * 动态是否点赞
	 */
	private Boolean isCollection;
	/**
	 * 投票
	 */
	private Object voteInfo;

	/**
	 * 圈子名称
	 */
	private String topicName;

}
