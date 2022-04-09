
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录表单
 *
 */
@Data
@ApiModel(value = "圈子用户列表请求")
public class TopicUserForm {
    @ApiModelProperty(value = "page",required = true)
    @NotBlank(message="page不能为空")
    private Integer page;

    @ApiModelProperty(value = "topicId",required = true)
    @NotBlank(message="topicId不能为空")
    private Integer id;

}
