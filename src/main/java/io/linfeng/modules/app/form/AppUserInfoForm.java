
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户信息请求体")
public class AppUserInfoForm {

    @ApiModelProperty(value = "用户id")
    private Integer uid;

}
