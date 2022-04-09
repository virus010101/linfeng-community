package io.linfeng.modules.app.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class TopicUpdateForm implements Serializable {
	private static final long serialVersionUID = 1L;


	private Integer id;
	/**
	 * 圈子名称
	 */
	@Length(max = 10, message = "名称不能超过10个字符")
	private String topicName;
	/**
	 * 描述
	 */
	@Length(max = 30, message = "描述不能超过30个字符")
	private String description;
	/**
	 * 圈子头像
	 */
	private String coverImage;
	/**
	 * 背景图
	 */
	private String bgImage;


}
