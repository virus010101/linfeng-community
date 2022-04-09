package io.linfeng.modules.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 敏感词库信息表
 * 
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-28 13:40:57
 */
@Data
@TableName("lf_sensitive")
public class SensitiveEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 敏感词库
	 */
	private String sensitiveWord;
	/**
	 * 是否开启 1-是 0-否
	 */
	private Integer state;
	/**
	 * 处理措施  1-禁止发布 2-需审核
	 */
	private Integer handleMeasures;

}
