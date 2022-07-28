
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "手机验证码登录")
public class SmsLoginForm {

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "验证码")
    private String code;
}
