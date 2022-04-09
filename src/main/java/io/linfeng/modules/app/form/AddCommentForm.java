
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "用户添加评论")
public class AddCommentForm {


    @ApiModelProperty(value = "pid")
    private Integer pid;

    @ApiModelProperty(value = "type")
    private Integer type;

    @ApiModelProperty(value = "toUid")
    private Integer toUid;

    @ApiModelProperty(value = "postId")
    private Long postId;

    @Length(max = 50, message = "评论内容不能超过50个字符")
    @NotBlank(message = "评论内容不能为空")
    @ApiModelProperty(value = "content")
    private String content;



}
