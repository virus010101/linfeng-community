
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "私聊列表请求体")
public class ChatListForm {

    @ApiModelProperty(value = "接收用户uid")
    private Integer uid;

    @ApiModelProperty(value = "page")
    private Integer page;

}
