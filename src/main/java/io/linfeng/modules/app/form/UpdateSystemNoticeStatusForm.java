
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "更新系统通知消息状态请求体")
public class UpdateSystemNoticeStatusForm {

    @ApiModelProperty(value = "帖子id")
    private Integer id;

}
