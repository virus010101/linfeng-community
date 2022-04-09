
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "私聊消息请求体")
public class ChatSendForm {

    @ApiModelProperty(value = "接收用户uid")
    private Integer uid;

    @NotBlank
    @Length(max = 100, message = "消息不能超过100个字符")
    @ApiModelProperty(value = "content")
    private String content;

}
