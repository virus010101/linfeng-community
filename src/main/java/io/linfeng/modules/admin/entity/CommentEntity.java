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
 * @date 2022-01-24 21:29:22
 */
@Data
@TableName("lf_comment")
public class CommentEntity implements Serializable {
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

}
