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
 * @date 2022-01-23 20:33:29
 */
@Data
@TableName("lf_topic_admin")
public class TopicAdminEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Integer id;
	/**
	 * 圈子id
	 */
	private Integer topicId;
	/**
	 * 用户id
	 */
	private Integer uid;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
