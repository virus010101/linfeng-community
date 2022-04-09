
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "UserJoinTopicForm")
public class UserJoinTopicForm {

    @ApiModelProperty(value = "topicClassId")
    private Integer classId;

    @ApiModelProperty(value = "page")
    private Integer page;

    @ApiModelProperty(value = "uid")
    private Integer uid;

}
