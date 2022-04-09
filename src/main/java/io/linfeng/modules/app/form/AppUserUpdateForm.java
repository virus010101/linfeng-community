
package io.linfeng.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * app用户信息修改
 *
 */
@Data
@ApiModel(value = "app用户信息修改")
public class AppUserUpdateForm {

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "性别")
    private Integer gender;

    @ApiModelProperty(value = "标签")
    private List<String> tagStr;

    @ApiModelProperty(value = "个性签名")
    @Length(max = 20, message = "个性签名不能超过20个字符")
    private String intro;
}
