
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "话题列表请求")
public class DiscussListForm {

    @ApiModelProperty(value = "page")
    private Integer page;

    @ApiModelProperty(value = "topicId")
    private Integer topicId;

}
