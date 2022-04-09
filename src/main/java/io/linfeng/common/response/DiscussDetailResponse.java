package io.linfeng.common.response;

import io.linfeng.modules.admin.entity.AppUserEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class DiscussDetailResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 话题标签id
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
	 * 标题
	 */
	private String title;
	/**
	 * 描述
	 */
	private String introduce;
	/**
	 * 浏览量
	 */
	private Integer readCount;
	/**
	 * 推荐位置：0 不推荐  1 首页推荐
	 */
	private Integer topType;
	/**
	 * 创建时间
	 */
	private Date createTime;

	private AppUserEntity userInfo;

	/**
	 * 帖子数量
	 */
	private Integer postCount;

}
