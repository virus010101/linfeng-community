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
 * @date 2022-01-19 16:29:48
 */
@Data
@TableName("lf_system")
public class SystemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private String config;
	/**
	 * 
	 */
	private String value;
	/**
	 * 
	 */
	private String extend;
	/**
	 * 
	 */
	private String intro;

}
