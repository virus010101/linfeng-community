
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 微信登录表单
 *
 *                  code: loginCode,
 * 					username: userInfo.nickName,
 * 					avatar: userInfo.avatarUrl,
 * 					province: userInfo.province,
 * 					city: userInfo.city,
 * 					gender: userInfo.gender
 */
@Data
@ApiModel(value = "微信登录表单")
public class WxLoginForm {

    @ApiModelProperty(value = "code",required = true)
    @NotBlank(message="code不能为空")
    private String code;

    @ApiModelProperty(value = "用户名",required = true)
    @NotBlank(message="用户名不能为空")
    private String username;

    @ApiModelProperty(value = "头像",required = true)
    @NotBlank(message="头像不能为空")
    private String avatar;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "性别")
    private Integer gender;

}
