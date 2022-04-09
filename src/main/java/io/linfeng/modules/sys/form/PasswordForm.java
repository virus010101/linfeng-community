
package io.linfeng.modules.sys.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 密码表单
 *
 */
@ApiModel(value="PasswordForm对象", description="密码表单请求参数对象")
@Data
public class PasswordForm {
    /**
     * 原密码
     */
    @ApiModelProperty(value = "原密码",required = true)
    private String password;
    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码",required = true)
    private String newPassword;

}
