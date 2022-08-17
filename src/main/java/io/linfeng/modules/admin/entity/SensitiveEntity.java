/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版演示站点: https://www.linfeng.tech
 * 商业版购买联系技术客服
 * QQ: 3582996245
 * 可正常分享和学习源码，不得专卖或非法牟利！
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
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
