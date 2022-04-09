
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "关注")
public class AddPostDeleteForm {

    @ApiModelProperty(value = "用户id")
    private Integer id;

}
