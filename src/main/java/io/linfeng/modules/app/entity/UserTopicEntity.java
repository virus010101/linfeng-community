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
 * @date 2022-01-23 21:24:46
 */
@Data
@TableName("lf_user_topic")
public class UserTopicEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
//	@TableId
	private Integer uid;
	/**
	 * 圈子id
	 */
	private Integer topicId;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
