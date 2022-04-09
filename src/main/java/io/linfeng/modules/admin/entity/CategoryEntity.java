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
 * @date 2022-01-21 14:32:52
 */
@Data
@TableName("lf_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类id
	 */
	@TableId
	private Integer cateId;
	/**
	 * 分类名称
	 */
	private String cateName;
	/**
	 * 是否推荐(1推荐)
	 */
	private Integer isTop;
	/**
	 * 图片
	 */
	private String coverImage;

}
