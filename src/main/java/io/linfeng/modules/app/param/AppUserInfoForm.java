/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 演示站点:https://www.linfengtech.cn
 * 可正常分享和学习源码，不得用于非法牟利！
 * 商业版购买联系技术客服 QQ: 3582996245
 * Copyright (c) 2021-2025 linfeng all rights reserved.
 * 版权所有，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.app.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户信息请求体")
public class AppUserInfoForm {

    @ApiModelProperty(value = "用户id")
    private Integer uid;

}
