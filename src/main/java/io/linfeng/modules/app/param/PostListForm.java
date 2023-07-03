/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 演示站点:https://www.linfeng.tech
 * 可正常分享和学习源码，不得用于非法牟利！
 * 商业版购买联系技术客服  QQ: 3582996245
 *  Copyright (c) 2021-2023 linfeng all rights reserved.
 * 版权所有，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.app.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "帖子列表分页")
public class PostListForm {

    @ApiModelProperty(value = "topicId")
    private Integer topicId;

    @ApiModelProperty(value = "page")
    private Integer page;

    @ApiModelProperty(value = "order排序")
    private String order;

    @ApiModelProperty(value = "uid")
    private Integer uid;

    @ApiModelProperty(value = "disId")
    private Integer disId;

    @ApiModelProperty(value = "分类id")
    private Integer classId;


}
