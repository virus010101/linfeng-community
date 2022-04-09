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
 * @date 2022-01-24 13:44:37
 */
@Data
@TableName("lf_discuss")
public class DiscussEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 话题标签id
	 */
	@TableId
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

}
