
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "轮播图列表请求体")
public class LinkListForm {

    @ApiModelProperty(value = "type")
    private Integer type;

    @ApiModelProperty(value = "page")
    private Integer page;

}
