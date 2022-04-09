
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "话题添加请求")
public class DiscussAddForm {

    @ApiModelProperty(value = "介绍")
    @NotBlank
    @Length(max = 50, message = "描述不能超过50个字符")
    private String introduce;

    @ApiModelProperty(value = "标题")
    @Length(max = 15, message = "标题不能超过15个字符")
    private String title;

    @ApiModelProperty(value = "圈子id")
    private Integer topicId;

}
