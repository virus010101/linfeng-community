/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 演示站点:https://www.linfengtech.cn
 * 可正常分享和学习源码，不得用于非法牟利！
 * 商业版购买联系技术客服  QQ: 3582996245
 *  Copyright (c) 2021-2025 linfeng all rights reserved.
 * 版权所有，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * 
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-24 20:49:32
 */
@Data
@TableName("lf_post_collection")
public class PostCollectionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
//	@TableId
	private Integer uid;
	/**
	 * 帖子id
	 */
	private Integer postId;

}
