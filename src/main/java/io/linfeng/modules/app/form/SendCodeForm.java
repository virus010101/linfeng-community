package io.linfeng.modules.app.form;

/**
 * @author linfeng
 * @date 2022/7/27 11:12
 */
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "手机验证码")
public class SendCodeForm {

    @ApiModelProperty(value = "手机号")
    private String mobile;

}
