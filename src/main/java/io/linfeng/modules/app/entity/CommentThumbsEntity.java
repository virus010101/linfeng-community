package io.linfeng.modules.app.entity;

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
 * @date 2022-01-25 19:00:24
 */
@Data
@TableName("lf_comment_thumbs")
public class CommentThumbsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 评论id
	 */
	private Integer cId;
	/**
	 * 用户id
	 */
	private Integer uid;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
