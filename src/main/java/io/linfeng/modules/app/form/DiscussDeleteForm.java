
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "话题删除请求")
public class DiscussDeleteForm {

    @ApiModelProperty(value = "id")
    private Integer id;


}
