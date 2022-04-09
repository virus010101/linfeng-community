
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "帖子点赞")
public class AddCollectionForm {

    @ApiModelProperty(value = "帖子id")
    private Integer id;

    @ApiModelProperty(value = "帖子的用户id")
    private Integer uid;

}
